package com.braincao.mmall.dao;

import com.braincao.mmall.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    Cart selectCartByUserIdProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);

    List<Cart> selectCartListByUserId(Integer userId);

    //看当前用户购物车是否全选(未勾选的个数为0即为全选了)
    int selectCartProductCheckedStatusByUserId(Integer userId);

    //移除购物车某个/些产品
    int deleteByUserIdProductList(@Param("userId") Integer userId, @Param("productList") List<String> productList);

    //设置当前用户购物车是否全选
    int updateCartProductCheckedStatusByUserId(@Param("userId") Integer userId, @Param("checked") Integer checked, @Param("productId") Integer productId);

    //设置当前用户购物车是否全选
    int getCartProductCount(Integer userId);

}