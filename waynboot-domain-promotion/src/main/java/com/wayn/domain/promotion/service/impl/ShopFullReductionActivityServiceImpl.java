package com.wayn.domain.promotion.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wayn.domain.api.promotion.entity.ShopFullReductionActivity;
import com.wayn.domain.api.promotion.mapper.ShopFullReductionActivityMapper;
import com.wayn.domain.api.promotion.service.IShopFullReductionActivityService;
import org.springframework.stereotype.Service;

/**
 * 满减活动服务实现。
 */
@Service
public class ShopFullReductionActivityServiceImpl
    extends ServiceImpl<ShopFullReductionActivityMapper, ShopFullReductionActivity>
    implements IShopFullReductionActivityService {
}
