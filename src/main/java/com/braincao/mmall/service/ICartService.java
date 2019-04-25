package com.braincao.mmall.service;

import com.braincao.mmall.common.ServerResponse;
import com.braincao.mmall.vo.CartVo;

public interface ICartService {

    //前台购物车添加商品
    ServerResponse<CartVo> addProduct(Integer userId, Integer productId, Integer count);

}
