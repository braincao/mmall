package com.braincao.mmall.controller.portal;

import com.braincao.mmall.common.Const;
import com.braincao.mmall.common.ResponseCode;
import com.braincao.mmall.common.ServerResponse;
import com.braincao.mmall.pojo.Shipping;
import com.braincao.mmall.pojo.User;
import com.braincao.mmall.service.IShippingService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/shipping")
public class ShippingController {

    @Autowired
    private IShippingService iShippingService;

    /**
     * 收货地址添加地址
     * @param session
     * @param shipping
     * @return
     */
    @RequestMapping("add.do")
    @ResponseBody
    //这里使用了SpringMVC对象绑定的方式，这样不用传那么多参数了
    public ServerResponse addShipping(HttpSession session, Shipping shipping) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(), ResponseCode.NEED_LOGIN.getMsg());
        }
        return iShippingService.addShipping(user.getId(), shipping);
    }

    /**
     * 收货地址删除地址
     * @param session
     * @param shippingId
     * @return
     */
    @RequestMapping("del.do")
    @ResponseBody
    public ServerResponse delShipping(HttpSession session, Integer shippingId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(), ResponseCode.NEED_LOGIN.getMsg());
        }
        return iShippingService.delShipping(user.getId(), shippingId);
    }

    /**
     * 登录状态更新地址
     * @param session
     * @param shipping
     * @return
     */
    @RequestMapping("update.do")
    @ResponseBody
    //这里使用了SpringMVC对象绑定的方式，这样不用传那么多参数了
    public ServerResponse updateShipping(HttpSession session, Shipping shipping) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(), ResponseCode.NEED_LOGIN.getMsg());
        }
        return iShippingService.updateShipping(user.getId(), shipping);
    }

    /**
     * 选中查看具体的地址
     * @param session
     * @param shippingId
     * @return
     */
    @RequestMapping("select.do")
    @ResponseBody
    public ServerResponse<Shipping> selectShipping(HttpSession session, Integer shippingId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(), ResponseCode.NEED_LOGIN.getMsg());
        }
        return iShippingService.selectShipping(user.getId(), shippingId);
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> getList(HttpSession session,
        @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
            User user = (User) session.getAttribute(Const.CURRENT_USER);
            if (user == null) {
                return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(), "用户未登录，请登录");
            }
            return iShippingService.searchProductListByIdOrName(user.getId(), pageNum, pageSize);
    }

}
