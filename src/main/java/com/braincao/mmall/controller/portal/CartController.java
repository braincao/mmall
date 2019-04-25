package com.braincao.mmall.controller.portal;

import com.braincao.mmall.common.Const;
import com.braincao.mmall.common.ResponseCode;
import com.braincao.mmall.common.ServerResponse;
import com.braincao.mmall.pojo.User;
import com.braincao.mmall.service.ICartService;
import com.braincao.mmall.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ICartService iCartService;

    /**
     * 购物车添加商品
     * @param session
     * @param productId
     * @param count
     * @return
     */
    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponse<CartVo> add(HttpSession session, Integer productId, Integer count) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(), ResponseCode.NEED_LOGIN.getMsg());
        }
        return iCartService.addProduct(user.getId(), productId, count);
    }

}
