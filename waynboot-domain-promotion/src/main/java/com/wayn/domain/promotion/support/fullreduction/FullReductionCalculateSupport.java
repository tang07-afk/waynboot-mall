package com.wayn.domain.promotion.support.fullreduction;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wayn.domain.api.promotion.entity.ShopFullReductionActivity;
import com.wayn.domain.api.promotion.entity.ShopFullReductionRule;
import com.wayn.domain.api.promotion.service.IShopFullReductionActivityService;
import com.wayn.domain.api.promotion.service.IShopFullReductionRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 满减优惠计算支撑类。

 查询当前有效活动、查询符合门槛的规则，并计算最终优惠金额

 规则：

 1.只计算已发布、未删除且处于有效时间内的活动。

 2.门槛按商品金额计算，不包含运费。

 3.单个活动取已命中的最高门槛档位。

 4.多个活动不叠加，取优惠金额最高的活动。
 */
@Component
@RequiredArgsConstructor
public class FullReductionCalculateSupport {

    private final IShopFullReductionActivityService activityService;
    private final IShopFullReductionRuleService ruleService;

    /**
     根据商品金额计算满减优惠金额。

     @param goodsPrice 商品金额，不包含运费

     @return 满减优惠金额；没有命中规则时返回 0
     */
    public BigDecimal calculate(BigDecimal goodsPrice) {
        if (goodsPrice == null || goodsPrice.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        Date now = new Date();

        List<ShopFullReductionActivity> activities = activityService.list(
            Wrappers.lambdaQuery(ShopFullReductionActivity.class)
                .eq(ShopFullReductionActivity::getStatus, 1)
                .eq(ShopFullReductionActivity::getDelFlag, false)
                .le(ShopFullReductionActivity::getStartTime, now)
                .ge(ShopFullReductionActivity::getEndTime, now)
        );

        BigDecimal bestReduction = BigDecimal.ZERO;

        for (ShopFullReductionActivity activity : activities) {
            if (activity == null || activity.getId() == null) {
                continue;
            }

            List<ShopFullReductionRule> rules = ruleService.list(
                Wrappers.lambdaQuery(ShopFullReductionRule.class)
                    .eq(ShopFullReductionRule::getActivityId, activity.getId())
                    .eq(ShopFullReductionRule::getDelFlag, false)
                    .le(ShopFullReductionRule::getThresholdAmount, goodsPrice)
                    .gt(ShopFullReductionRule::getReductionAmount, BigDecimal.ZERO)
                    .orderByDesc(ShopFullReductionRule::getThresholdAmount)
            );

            // 按门槛降序排列，第一条就是该活动命中的最高门槛档位。
            if (rules.isEmpty()) {
                continue;
            }

            ShopFullReductionRule matchedRule = rules.get(0);
            BigDecimal reduction = matchedRule.getReductionAmount();

            if (reduction == null || reduction.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }

            if (reduction.compareTo(bestReduction) > 0) {
                bestReduction = reduction;
            }

        }

// 防止异常配置导致优惠金额超过商品金额。
        return bestReduction.min(goodsPrice);
    }
}
