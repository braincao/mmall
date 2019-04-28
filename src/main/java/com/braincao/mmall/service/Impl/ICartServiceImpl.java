package com.braincao.mmall.service.Impl;

import com.braincao.mmall.common.Const;
import com.braincao.mmall.common.ResponseCode;
import com.braincao.mmall.common.ServerResponse;
import com.braincao.mmall.dao.CartMapper;
import com.braincao.mmall.dao.ProductMapper;
import com.braincao.mmall.pojo.Cart;
import com.braincao.mmall.pojo.Product;
import com.braincao.mmall.service.ICartService;
import com.braincao.mmall.util.BigDecimalUtil;
import com.braincao.mmall.vo.CartProductVo;
import com.braincao.mmall.vo.CartVo;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service("iCartService")
//定义iCartService名字，就可以在controller层自动完成注入
public class ICartServiceImpl implements ICartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    //前台购物车添加商品，并前台根据返回的CartVo展示购物车
    @Override
    public ServerResponse<CartVo> addProduct(Integer userId, Integer productId, Integer count) {
        if(productId==null || count==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getStatus(), ResponseCode.ILLEGAL_ARGUMENT.getMsg());
        }
        Cart cart = cartMapper.selectCartByUserIdProductId(userId, productId);
        if (cart == null) {
            //这个产品不在这个购物车里，需要新增这个产品的记录
            Cart cartItem = new Cart();
            cartItem.setUserId(userId);
            cartItem.setProductId(productId);
            cartItem.setQuantity(count);
            cartItem.setChecked(Const.Cart.CHECKED);
            cartMapper.insert(cartItem);
        } else {
            //这个产品在这个购物车里，需要增加这个产品的数量
            count = cart.getQuantity() + count;
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        //用户添加商品到购物车后，前台的状态需要根据库存、价格来展示，因此需要下面的VO来计算并封装，展示给前台
        return listProduct(userId);
    }

    /**
     * 封装当前用户的购物车VO
     * 用户操作完购物车后，前台的状态需要根据库存、价格来展示，因此需要下面的VO来计算并封装，展示给前台
     * 此方法将伴随购物车操作经常使用
     * @param userId
     * @return
     */
    private CartVo getCartVoLimit(Integer userId) {
        CartVo cartVo = new CartVo();
        List<Cart> cartList = cartMapper.selectCartListByUserId(userId);

        //计算购物车中所选商品的总价
        BigDecimal cartTotalPrice = new BigDecimal("0");//BigDecimal构造器参数一定要使用String才准确
        List<CartProductVo> cartProductVoList = Lists.newArrayList();

        if(CollectionUtils.isNotEmpty(cartList)){
            for(Cart cartItem: cartList){
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(cartItem.getId());
                cartProductVo.setUserId(cartItem.getUserId());
                cartProductVo.setProductId(cartItem.getProductId());

                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
                if(product!=null){
                    cartProductVo.setProductChecked(cartItem.getChecked());
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductSubtitle(product.getSubtitle());
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStock(product.getStock());

                    //判断库存，即计算cartProductVo的quantity字段
                    if(product.getStock() >= cartItem.getQuantity()){
                        //库存充足时
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                        cartProductVo.setQuantity(cartItem.getQuantity());
                    }else{
                        //当库存不够时，需要把购物车中用户购买的数量更新为库存数量
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);

                        //购物车中更新有效库存
                        Cart cartForQuantity = new Cart();
                        cartForQuantity.setId(cartItem.getId());
                        cartForQuantity.setQuantity(product.getStock());
                        cartMapper.updateByPrimaryKeySelective(cartForQuantity);
                        cartProductVo.setQuantity(cartItem.getQuantity());
                    }

                    //计算总价
                    cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(), cartProductVo.getQuantity()));
                }
                //如果购物车中的此产品被勾选，那么当前购物车的总价被修改
                if(cartItem.getChecked()==Const.Cart.CHECKED){
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(), cartProductVo.getProductPrice().doubleValue());
                }

                cartProductVoList.add(cartProductVo);
            }

        }
        cartVo.setCartProductVoList(cartProductVoList);
        cartVo.setProductTotalPrice(cartTotalPrice);
        cartVo.setAllChecked(this.getAllCheckedStatus(userId));
        cartVo.setImageHost("http://localhost:8080/upload/");

        return cartVo;
    }

    //检查当前用户购物车的商品是否全选
    private boolean getAllCheckedStatus(Integer userId){
        if(userId==null){
            return false;
        }
        return cartMapper.selectCartProductCheckedStatusByUserId(userId)==0;
    }

    //更新购物车某个产品数量，并前台根据返回的CartVo展示购物车
    @Override
    public ServerResponse<CartVo> updateProduct(Integer userId, Integer productId, Integer count) {
        if(productId==null || count==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getStatus(), ResponseCode.ILLEGAL_ARGUMENT.getMsg());
        }
        Cart cart = cartMapper.selectCartByUserIdProductId(userId, productId);
        if (cart != null) {
            //这个产品在这个购物车里，需要更新这个产品的记录
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        //更新购物车后，前台的状态需要根据库存、价格来展示，因此需要下面的VO来计算并封装，展示给前台
        return listProduct(userId);
    }

    //移除购物车某个/些产品
    @Override
    public ServerResponse<CartVo> deleteProduct(Integer userId, String productIds){
        List<String> productList = Splitter.on(",").splitToList(productIds);
        if(CollectionUtils.isEmpty(productList)){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getStatus(), ResponseCode.ILLEGAL_ARGUMENT.getMsg());
        }
        cartMapper.deleteByUserIdProductList(userId, productList);
        //更新购物车后，前台的状态需要根据库存、价格来展示，因此需要下面的VO来计算并封装，展示给前台
        return listProduct(userId);
    }

    //购物车List列表
    @Override
    public ServerResponse<CartVo> listProduct(Integer userId){
        //更新购物车后，前台的状态需要根据库存、价格来展示，因此需要下面的VO来计算并封装，展示给前台
        CartVo cartVo = this.getCartVoLimit(userId);
        return ServerResponse.createBySuccessData(cartVo);
    }

    //购物车全选、全反选、单独选、单独反选
    @Override
    public ServerResponse<CartVo> selectOrUnSelectAllProduct(Integer userId, Integer checked, Integer productId){
        cartMapper.updateCartProductCheckedStatusByUserId(userId, checked, productId);
        return listProduct(userId);
    }

    //查询在购物车里的产品数量
    @Override
    public ServerResponse<Integer> getCartProductCount(Integer userId){
        if(userId==null){
            return ServerResponse.createBySuccessData(0);
        }
        int row = cartMapper.getCartProductCount(userId);
        return ServerResponse.createBySuccessData(row);
    }
}
