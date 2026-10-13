package com.wayn.domain.promotion.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wayn.domain.api.promotion.entity.ShopFullReductionRule;
import com.wayn.domain.api.promotion.mapper.ShopFullReductionRuleMapper;
import com.wayn.domain.api.promotion.service.IShopFullReductionRuleService;
import org.springframework.stereotype.Service;

/**

 * 满减规则服务实现。
 */
@Service
public class ShopFullReductionRuleServiceImpl
    extends ServiceImpl<ShopFullReductionRuleMapper, ShopFullReductionRule>
    implements IShopFullReductionRuleService {
}
