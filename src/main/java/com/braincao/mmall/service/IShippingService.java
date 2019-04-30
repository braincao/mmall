package com.braincao.mmall.service;

import com.braincao.mmall.common.ServerResponse;
import com.braincao.mmall.pojo.Shipping;
import com.github.pagehelper.PageInfo;

public interface IShippingService {

    //收货地址添加地址
    ServerResponse addShipping(Integer userId, Shipping shipping);

    //收货地址删除地址
    ServerResponse delShipping(Integer userId, Integer shippingId);

    //登录状态更新地址
    ServerResponse updateShipping(Integer userId, Shipping shipping);

    ServerResponse<Shipping> selectShipping(Integer userId, Integer shippingId);

    ServerResponse<PageInfo> searchProductListByIdOrName(Integer userId, int pageNum, int pageSize);

}
