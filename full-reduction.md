# 满减优惠功能设计说明


## 1. 功能背景

为什么新增满减：

- 原项目已有优惠券能力
- 优惠券属于用户领取型优惠
- 满减属于平台营销规则

因此拆分独立领域。


---

## 2. 功能目标

支持：

- 满 N 减 M
- 多阶梯规则
- 自动匹配最高优惠
- 与订单流程无侵入结合


---

## 3. 数据模型


shop_full_reduction_activity

活动表


shop_full_reduction_rule

规则表


shop_order

新增：

full_reduction_price


---

## 4. 下单链路


用户提交订单

↓

计算商品金额

↓

查询有效满减活动

↓

匹配最高优惠规则

↓

写入订单优惠金额

↓

计算实际支付金额

↓

继续库存、支付流程


---

## 5. 计算规则


示例：


商品金额：

748


规则：

满300减50

满200减30

满100减10


匹配：

满300减50


最终：


商品金额 748

满减优惠 -50

实际支付 698


---

## 6. 代码结构


FullReductionCalculateSupport

负责：

- 活动查询
- 规则匹配
- 优惠计算


OrderSubmitSupport

负责：

- 下单流程接入


OrderSubmitContext

新增：

fullReductionPrice


---

## 7. 数据初始化


执行：


db-init/07_full_reduction.sql


包含：

- 活动数据
- 阶梯规则


---

## 8. 测试流程


1. 启动环境


docker compose up -d


2. 执行 SQL


3. 提交订单


请求：

POST xxx/order/submit


4. 查询订单


验证：

商品金额 748

满减优惠 50

实际支付 698


