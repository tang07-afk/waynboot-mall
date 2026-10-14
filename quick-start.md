# waynboot-mall 快速启动与满减功能验证指南


## 1. 项目介绍

`waynboot-mall` 是一个基于 Spring Boot 3、Java 17 的多模块商城后端项目。

当前已实现：

- 商品管理
- 购物车
- 订单提交
- Redis 库存预扣减
- MySQL 库存冻结
- RabbitMQ 异步处理
- 本地消息补偿
- 支付流水
- 秒杀活动
- 满减优惠


本文主要用于：

- 新环境快速启动
- 下单链路验证
- 满减优惠功能测试


---

# 2. 环境要求


## 开发环境

建议：

| 环境 | 版本 |
|-|-|
| JDK | 17 |
| Maven | 3.8+ |
| MySQL | 8.0 |
| Redis | 6+ |
| RabbitMQ | 3+ |
| Elasticsearch | 7.x |
| Docker | 20+ |


---

# 3. 克隆项目


git clone https://github.com/tang07-afk/waynboot-mall.git

cd waynboot-mall


# 4. 启动基础环境

项目提供 Docker Compose：

     启动业务中间件： docker compose -f docker-compose-es7-rabbitmq-redis-mysql.yml up -d

     启动应用依赖：docker compose up -d

查看状态：
      
     docker ps

正常情况下：
    
    mysql             Up
    redis             Up
    rabbitmq          Up
    elasticsearch     Up
    admin             Up
    mobile            Up
    message           Up
    nginx             Up    

# 5. 数据库初始化

数据库：

    wayn_shop

初始化sql脚本顺序：

    db-init
    │
    ├── 01_wayn_shop_20230719.sql
    ├── 02_inventory_stock.sql
    ├── 03_local_message.sql
    ├── 04_order_status_log.sql
    ├── 05_payment_flow.sql
    ├── 06_seckill.sql
    └── 07_full_reduction.sql

# 6. 查看满减活动数据

执行sql语句：
   
    select *
    from shop_full_reduction_activity;

    select *
    from shop_full_reduction_rule;

# 7.主要测试接口

下单接口：

    POST /order/submit

订单详情接口：

    GET /order/detail/{orderSn}

# 8. 本地开发环境测试

1. 启动项目：
   
    启动基础环境：
        
       docker compose -f docker-compose-es7-rabbitmq-redis-mysql.yml up -d

   启动后端服务：
   
    启动移动端接口：
       
       mvn -pl waynboot-mobile-api spring-boot:run

    启动消息消费者：

       mvn -pl waynboot-message/waynboot-message-consumer spring-boot:run

    启动后台服务：
       
       mvn -pl waynboot-admin-api spring-boot:run
      
2. 登录用户数据准备

   移动端登录后：系统会根据手机号查询shop_member，如果不存在，自动创建会员。登录成功后得到userId，后续，购物车shop_cart、地址shop_address必须属于当前登录用户。


3. 购物车数据准备
   
   查询当前用户购物车:

       select *
       from shop_cart
       where user_id = 当前用户id;

   返回结果中，需要满足满减活动规则的数据，其中这几行的checked = 1，并且def_flag = 0。如果没有，请插入数据。


4. 收货地址准备

   查询当前用户收货地址:
   
       select *
       from shop_address
       where user_id = 当前用户id;
    获取address_id，如果没有，请插入数据。


5. Mobile端登录测试(推荐使用apifox)

   第一步：发送验证码
   
   接口：
   
       POST /mobile/genMobileCode

   请求：

       {
          "mobile":"13800138000"
       }

    开发环境验证码固定：1234

    第二步：手机号登录

    接口：

        POST /mobile/login

    请求：

       {
          "mobile":"13800138000",
          "yzm":"1234"
       }

    返回：

       {
         "code":200,
         "data":"token"
       }

    保存：Authorization: token，后续订单接口都携带


6. 下单接口测试

    接口：
       
       POST /order/submit

    Header：

       Authorization: token

    Body：

       {
          "orderSn":null,
          "cartIdArr":[xxx,xxx],
          "addressId":xxxx,
          "userCouponId":null,
          "message":"满减测试订单",
          "payType":1,
          "returnUrl":""
       }
# 9. 核心代码位置

满减计算：

     waynboot-domain-promotion

       └── support

         └── fullreduction

           └── FullReductionCalculateSupport

订单接入：
  
     waynboot-domain-trade

      └── support

        └── order

          ├── OrderSubmitSupport
          └── OrderSubmitContext

订单持久化：

    waynboot-domain-trade

     └── mapper

       └── OrderMapper.xml


     

