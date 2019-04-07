package com.braincao.mmall.dao;

import com.braincao.mmall.pojo.Product;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> selectList();

    List<Product> searchProductListByIdAndName(@RequestParam("productName") String productName, @RequestParam("productId") Integer productId);
}