CREATE TABLE IF NOT EXISTS `shop_full_reduction_activity` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `name` VARCHAR(100) NOT NULL COMMENT '满减活动名称',
    `brief` VARCHAR(255) DEFAULT NULL COMMENT '活动简介',
    `start_time` DATETIME NOT NULL COMMENT '活动开始时间',
    `end_time` DATETIME NOT NULL COMMENT '活动结束时间',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0 草稿，1 已发布，2 已下架',
    `sort_order` INT NOT NULL DEFAULT 100 COMMENT '排序值',
    `create_time` DATETIME NOT NULL COMMENT '创建时间',
    `update_time` DATETIME NOT NULL COMMENT '更新时间',
    `del_flag` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '删除标志：0 存在，1 删除',
    PRIMARY KEY (`id`),
    KEY `idx_full_reduction_status_time` (`status`, `start_time`, `end_time`),
    KEY `idx_full_reduction_sort` (`sort_order`)
    ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4
    COLLATE=utf8mb4_general_ci COMMENT='满减活动表';


CREATE TABLE IF NOT EXISTS `shop_full_reduction_rule` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `activity_id` BIGINT NOT NULL COMMENT '满减活动 ID',
    `threshold_amount` DECIMAL(10,2) NOT NULL COMMENT '满减门槛金额',
    `reduction_amount` DECIMAL(10,2) NOT NULL COMMENT '优惠金额',
    `sort_order` INT NOT NULL DEFAULT 100 COMMENT '排序值',
    `create_time` DATETIME NOT NULL COMMENT '创建时间',
    `update_time` DATETIME NOT NULL COMMENT '更新时间',
    `del_flag` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '删除标志：0 存在，1 删除',
    PRIMARY KEY (`id`),
    KEY `idx_full_reduction_rule_activity` (`activity_id`, `del_flag`, `threshold_amount`)
    ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4
    COLLATE=utf8mb4_general_ci COMMENT='满减规则表';


INSERT INTO shop_full_reduction_activity
(
    name,
    brief,
    start_time,
    end_time,
    status,
    sort_order,
    create_time,
    update_time,
    del_flag
)
VALUES
(
    '新人满减活动',
    '订单满额立减优惠',
    '2026-01-01 00:00:00',
    '2026-12-31 23:59:59',
    1,
    100,
    NOW(),
    NOW(),
    0
);


INSERT INTO shop_full_reduction_rule
(
    activity_id,
    threshold_amount,
    reduction_amount,
    sort_order,
    create_time,
    update_time,
    del_flag
)
VALUES
(
    1,
    100.00,
    10.00,
    100,
    NOW(),
    NOW(),
    0
),
(
    1,
    200.00,
    30.00,
    90,
    NOW(),
    NOW(),
    0
),
(
    1,
    300.00,
    50.00,
    80,
    NOW(),
    NOW(),
    0
);
