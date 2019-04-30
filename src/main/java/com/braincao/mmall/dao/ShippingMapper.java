package com.braincao.mmall.dao;

import com.braincao.mmall.pojo.Product;
import com.braincao.mmall.pojo.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    int deleteByShippingIdUserId(@Param("shippingId") Integer shippingId, @Param("userId") Integer userId);

    int updateByUserId(Shipping record);

    Shipping selectByShippingIdUserId(@Param("shippingId") Integer shippingId, @Param("userId") Integer userId);

    List<Shipping> selectByUserId(Integer userId);

}