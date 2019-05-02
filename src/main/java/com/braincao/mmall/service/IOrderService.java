package com.braincao.mmall.service;

import com.braincao.mmall.common.ServerResponse;
import com.braincao.mmall.vo.CartVo;

public interface IOrderService {

    //前台购物车添加商品
    public ServerResponse<CartVo> pay(Integer userId, Long orderNo, String path);

}
