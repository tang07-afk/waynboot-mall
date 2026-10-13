package com.wayn.domain.trade.support.order;

import com.wayn.domain.api.trade.entity.Address;
import com.wayn.domain.api.cart.entity.Cart;

import java.math.BigDecimal;
import java.util.List;

/**
 * 下单上下文。
 * 收敛下单金额计算和订单落库前需要的聚合数据，便于编排层和组装层之间传递。
 *
 *
 * 商品金额 → 运费 → 满减 → 订单金额 → 优惠券 → 实付金额
 */
public record OrderSubmitContext(Address address,
                                 List<Cart> checkedGoodsList,
                                 BigDecimal checkedGoodsPrice,
                                 BigDecimal freightPrice,
                                 BigDecimal fullReductionPrice,
                                 BigDecimal orderTotalPrice,
                                 BigDecimal couponPrice,
                                 BigDecimal actualPrice) {
}
