package com.wayn.domain.api.promotion.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 满减规则表。
 * 一个活动可以配置多个门槛及对应的优惠金额。
 */
@Data
@TableName("shop_full_reduction_rule")
public class ShopFullReductionRule implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long activityId;

    private BigDecimal thresholdAmount;

    private BigDecimal reductionAmount;

    private Integer sortOrder;

    private Date createTime;

    private Date updateTime;

    private Boolean delFlag;
}
