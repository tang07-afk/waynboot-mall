package com.wayn.domain.promotion.support.fullreduction;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.wayn.domain.api.promotion.entity.ShopFullReductionActivity;
import com.wayn.domain.api.promotion.entity.ShopFullReductionRule;
import com.wayn.domain.api.promotion.service.IShopFullReductionActivityService;
import com.wayn.domain.api.promotion.service.IShopFullReductionRuleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * 满减计算测试。
 */
class FullReductionCalculateSupportTest {

    private IShopFullReductionActivityService activityService;
    private IShopFullReductionRuleService ruleService;
    private FullReductionCalculateSupport support;

    @BeforeEach
    void setUp() {
        activityService = mock(IShopFullReductionActivityService.class);
        ruleService = mock(IShopFullReductionRuleService.class);
        support = new FullReductionCalculateSupport(activityService, ruleService);
    }

    /**
     * 商品金额低于最低门槛时，不产生优惠。
     */
    @Test
    void returnsZeroWhenGoodsPriceBelowThreshold() {
        mockActivities(activity(1L));
        mockRules();

        assertAmount("0", support.calculate(new BigDecimal("80.00")));
    }

    /**
     * 商品金额恰好达到门槛时，应命中该档规则。
     */
    @Test
    void appliesReductionWhenGoodsPriceEqualsThreshold() {
        mockActivities(activity(1L));
        mockRules(rule(1L, "100", "10"));

        assertAmount("10.00", support.calculate(new BigDecimal("100.00")));
    }

    /**
     * 商品金额处于两个门槛之间时，命中较低的已满足门槛。
     */
    @Test
    void appliesHighestQualifiedThreshold() {
        mockActivities(activity(1L));
        mockRules(rule(1L, "100", "10"));

        assertAmount("10.00", support.calculate(new BigDecimal("199.00")));
    }

    /**
     * 商品金额达到更高门槛时，使用更高档位的优惠。
     */
    @Test
    void appliesHigherTierWhenThresholdReached() {
        mockActivities(activity(1L));
        mockRules(
            rule(1L, "200", "30"),
            rule(1L, "100", "10")
        );

        assertAmount("30.00", support.calculate(new BigDecimal("200.00")));
    }

    /**
     * 商品金额达到最高档位时，返回最高档位的优惠。
     */
    @Test
    void appliesHighestTierForLargeGoodsPrice() {
        mockActivities(activity(1L));
        mockRules(
            rule(1L, "300", "50"),
            rule(1L, "200", "30"),
            rule(1L, "100", "10")
        );

        assertAmount("50.00", support.calculate(new BigDecimal("350.00")));
    }

    /**
     * 多个活动都命中时，只取优惠金额最高的活动，不叠加优惠。
     */
    @Test
    void doesNotStackMultipleActivities() {
        mockActivities(activity(1L), activity(2L));

        when(ruleService.list(any(Wrapper.class)))
            .thenReturn(List.of(rule(1L, "100", "10")))
            .thenReturn(List.of(rule(2L, "200", "30")));

        assertAmount("30.00", support.calculate(new BigDecimal("250.00")));
    }

    private void mockActivities(ShopFullReductionActivity... activities) {
        when(activityService.list(any(Wrapper.class)))
            .thenReturn(List.of(activities));
    }

    private void mockRules(ShopFullReductionRule... rules) {
        when(ruleService.list(any(Wrapper.class)))
            .thenReturn(List.of(rules));
    }

    private ShopFullReductionActivity activity(Long id) {
        ShopFullReductionActivity activity = new ShopFullReductionActivity();
        activity.setId(id);
        activity.setStatus(1);
        activity.setDelFlag(false);
        return activity;
    }

    private ShopFullReductionRule rule(
        Long activityId, String threshold, String reduction) {
        ShopFullReductionRule rule = new ShopFullReductionRule();
        rule.setActivityId(activityId);
        rule.setThresholdAmount(new BigDecimal(threshold));
        rule.setReductionAmount(new BigDecimal(reduction));
        rule.setDelFlag(false);
        return rule;
    }

    private void assertAmount(String expected, BigDecimal actual) {
        assertEquals(0, new BigDecimal(expected).compareTo(actual),
            "满减金额不符合预期");
    }
}
