package com.braincao.mmall.controller.backend;

import com.braincao.mmall.common.Const;
import com.braincao.mmall.common.ResponseCode;
import com.braincao.mmall.common.ServerResponse;
import com.braincao.mmall.pojo.Product;
import com.braincao.mmall.pojo.User;
import com.braincao.mmall.service.IProductService;
import com.braincao.mmall.service.IUserService;
import com.braincao.mmall.vo.ProductDetailVo;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IProductService iProductService;

    /**
     * 后台 新增OR更新产品
     *
     * @param session
     * @param product
     * @return
     */
    @RequestMapping("save.do")
    @ResponseBody
    public ServerResponse productSave(HttpSession session, Product product) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(), "用户未登录，请登录");
        }
        //校验是否是管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //是管理员，填充我们新增OR更新产品的业务逻辑
            return iProductService.addOrUpdateProduct(product);

        }
        return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
    }


    /**
     * 产品上下架
     *
     * @param productId
     * @param status
     * @return
     */
    @RequestMapping("set_sale_status.do")
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session, Integer productId, Integer status) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(), "用户未登录，请登录");
        }
        //校验是否是管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //是管理员，填充我们产品上下架的业务逻辑
            return iProductService.setSaleStatus(productId, status);

        }
        return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
    }

    /**
     * 产品详情
     *
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse<ProductDetailVo> detail(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(), "用户未登录，请登录");
        }
        //校验是否是管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //是管理员，填充我们产品详情的业务逻辑
            return iProductService.detail(productId);

        }
        return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
    }

    /**
     * 后台 获取产品list
     * 涉及分页功能，采用mybatis pagehelper分页
     *
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> getList(HttpSession session,
                                            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(), "用户未登录，请登录");
        }
        //校验是否是管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //是管理员，填充我们获取产品list的业务逻辑
            return iProductService.getProductList(pageNum, pageSize);

        }
        return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
    }

    /**
     * 产品搜索:通过productId、productName等搜索，且结果进行分页
     * @param session
     * @param productName
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse<PageInfo> getList(HttpSession session, String productName, Integer productId,
                                            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(), "用户未登录，请登录");
        }
        //校验是否是管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //是管理员，填充我们产品搜索的业务逻辑
            return iProductService.searchProductListByIdOrName(String productName, Integer productId,, pageNum, pageSize);

        }
        return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
    }
}
