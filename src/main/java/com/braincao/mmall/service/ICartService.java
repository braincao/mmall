package com.braincao.mmall.service;

import com.braincao.mmall.common.ServerResponse;
import com.braincao.mmall.vo.CartVo;

public interface ICartService {

    //前台购物车添加商品
    ServerResponse<CartVo> addProduct(Integer userId, Integer productId, Integer count);

    //更新购物车某个产品数量
    ServerResponse<CartVo> updateProduct(Integer userId, Integer productId, Integer count);

    //移除购物车某个产品
    ServerResponse<CartVo> deleteProduct(Integer userId, String productIds);

    //购物车List列表
    ServerResponse<CartVo> listProduct(Integer userId);

    //购物车全选、全反选、单独选、单独反选
    ServerResponse<CartVo> selectOrUnSelectAllProduct(Integer userId, Integer checked, Integer productId);

    //查询在购物车里的产品数量
    ServerResponse<Integer> getCartProductCount(Integer userId);

}
