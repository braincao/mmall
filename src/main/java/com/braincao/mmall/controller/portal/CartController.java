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

    /**
     * 更新购物车某个产品数量
     * @param session
     * @param productId
     * @param count
     * @return
     */
    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse<CartVo> updateProduct(HttpSession session, Integer productId, Integer count) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(), ResponseCode.NEED_LOGIN.getMsg());
        }
        return iCartService.updateProduct(user.getId(), productId, count);
    }

    /**
     * 移除购物车某个产品
     * @param session
     * @param productIds 前台传递的需要删除的产品参数，eg 1,3--即删除1/3两个产品
     * @return
     */
    @RequestMapping("delete_product.do")
    @ResponseBody
    public ServerResponse<CartVo> deleteProduct(HttpSession session, String productIds) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(), ResponseCode.NEED_LOGIN.getMsg());
        }
        return iCartService.deleteProduct(user.getId(), productIds);
    }

    /**
     * 购物车List列表
     * @param session
     * @return
     */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<CartVo> listProduct(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(), ResponseCode.NEED_LOGIN.getMsg());
        }
        return iCartService.listProduct(user.getId());
    }

    //全选、全反选、单独选、单独反选：
    /**
     * 购物车全选
     * @param session
     * @return
     */
    @RequestMapping("select_all.do")
    @ResponseBody
    public ServerResponse<CartVo> SelectAllProduct(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(), ResponseCode.NEED_LOGIN.getMsg());
        }
        return iCartService.selectOrUnSelectAllProduct(user.getId(), Const.Cart.CHECKED, null);
    }

    /**
     * 购物车全反选
     * @param session
     * @return
     */
    @RequestMapping("un_select_all.do")
    @ResponseBody
    public ServerResponse<CartVo> UnSelectAllProduct(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(), ResponseCode.NEED_LOGIN.getMsg());
        }
        return iCartService.selectOrUnSelectAllProduct(user.getId(), Const.Cart.UN_CHECKED, null);
    }

    /**
     * 单独选
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping("select.do")
    @ResponseBody
    public ServerResponse<CartVo> SelectProduct(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(), ResponseCode.NEED_LOGIN.getMsg());
        }
        return iCartService.selectOrUnSelectAllProduct(user.getId(), Const.Cart.CHECKED, productId);
    }

    /**
     * 单独选
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping("un_select.do")
    @ResponseBody
    public ServerResponse<CartVo> UnSelectProduct(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(), ResponseCode.NEED_LOGIN.getMsg());
        }
        return iCartService.selectOrUnSelectAllProduct(user.getId(), Const.Cart.UN_CHECKED, productId);
    }

    /**
     * 查询在购物车里的产品数量
     * @param session
     * @return
     */
    @RequestMapping("get_cart_product_count.do")
    @ResponseBody
    public ServerResponse<Integer> getCartProductCount(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createBySuccessData(0);
        }
        return iCartService.getCartProductCount(user.getId());
    }


}
