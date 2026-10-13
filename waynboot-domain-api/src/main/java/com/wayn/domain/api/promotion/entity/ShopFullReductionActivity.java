package com.wayn.domain.api.promotion.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 满减活动主表。
 * 保存活动基本信息、有效时间和发布状态。
 */
@Data
@TableName("shop_full_reduction_activity")
public class ShopFullReductionActivity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String brief;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 状态：0 草稿，1 已发布，2 已下架。
     */
    private Integer status;

    private Integer sortOrder;

    private Date createTime;

    private Date updateTime;

    private Boolean delFlag;
}
