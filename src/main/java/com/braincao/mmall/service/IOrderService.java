package com.braincao.mmall.service;

import com.braincao.mmall.common.ServerResponse;
import com.braincao.mmall.vo.CartVo;
import com.braincao.mmall.vo.OrderProductVo;
import com.braincao.mmall.vo.OrderVo;
import com.github.pagehelper.PageInfo;

import java.util.Map;

public interface IOrderService {

    //通过userId、orderNo查询到用户的订单，然后通过支付宝当面付流程生成支付二维码，保存到path中传给前端
    public ServerResponse pay(Integer userId, Long orderNo, String path);

    public ServerResponse queryOrderPayStatus(Integer userId, Long orderNo);

    public ServerResponse alipayCallback(Map<String, String> params);

    //创建订单
    public ServerResponse createOrder(Integer userId, Integer shippingId);

    //在未付款的情况下取消订单
    public ServerResponse<String> cancel(Integer userId,Long orderNo);

    //获取订单的商品信息--获取购物车中已经选中的商品详情
    public ServerResponse<OrderProductVo> getOrderCartProduct(Integer userId);

    //订单详情detail
    public ServerResponse<OrderVo> getOrderDetail(Integer userId, Long orderNo);

    //订单list+分页
    public ServerResponse<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize);

    //以下是backend管理员后台接口
    //后台：list
    public ServerResponse<PageInfo> manageList(int pageNum,int pageSize);

    //后台订单详情
    public ServerResponse<OrderVo> manageDetail(Long orderNo);

    //后台：按订单号查询
    public ServerResponse<PageInfo> manageSearch(Long orderNo,int pageNum,int pageSize);

    //后台：订单发货
    public ServerResponse<String> manageSendGoods(Long orderNo);

}
