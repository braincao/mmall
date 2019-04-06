package com.braincao.mmall.service;

import com.braincao.mmall.common.ServerResponse;
import com.braincao.mmall.pojo.Product;
import com.braincao.mmall.vo.ProductDetailVo;

public interface IProductService {

    //后台 新增OR更新产品
    ServerResponse addOrUpdateProduct(Product product);

    //后台 产品上下架
    ServerResponse<String> setSaleStatus(Integer productId, Integer status);

    //后台 产品详情
    ServerResponse<ProductDetailVo> detail(Integer productId);

}
