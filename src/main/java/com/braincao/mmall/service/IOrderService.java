package com.braincao.mmall.service;

import com.braincao.mmall.common.ServerResponse;
import com.braincao.mmall.vo.CartVo;

import java.util.Map;

public interface IOrderService {

    //通过userId、orderNo查询到用户的订单，然后通过支付宝当面付流程生成支付二维码，保存到path中传给前端
    public ServerResponse pay(Integer userId, Long orderNo, String path);

    public ServerResponse queryOrderPayStatus(Integer userId, Long orderNo);

    public ServerResponse alipayCallback(Map<String, String> params);

}
