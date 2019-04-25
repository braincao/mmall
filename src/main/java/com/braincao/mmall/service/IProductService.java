package com.braincao.mmall.service;

import com.braincao.mmall.common.ServerResponse;
import com.braincao.mmall.pojo.Product;
import com.braincao.mmall.vo.ProductDetailVo;
import com.github.pagehelper.PageInfo;

public interface IProductService {

    //后台 新增OR更新产品
    ServerResponse addOrUpdateProduct(Product product);

    //后台 产品上下架
    ServerResponse<String> setSaleStatus(Integer productId, Integer status);

    //后台 产品详情
    ServerResponse<ProductDetailVo> detail(Integer productId);

    //后台 获取产品list。采用mybatis pageHelper分页
    ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);

    //后台 产品搜索:通过productId、productName等搜索，且结果进行分页
    ServerResponse<PageInfo> searchProductListByIdOrName(String productName, Integer productId, int pageNum, int pageSize);

    //前台 产品详情
    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    //前台产品搜索:通过categoryId、keyword等搜索，且结果进行分页
    ServerResponse<PageInfo> searchProductListByIdOrKeyword(Integer categoryId, String keyword, int pageNum, int pageSize, String orderBy);

}
