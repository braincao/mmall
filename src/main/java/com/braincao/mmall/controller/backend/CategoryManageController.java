package com.braincao.mmall.controller.backend;

import com.braincao.mmall.common.Const;
import com.braincao.mmall.common.ResponseCode;
import com.braincao.mmall.common.ServerResponse;
import com.braincao.mmall.pojo.User;
import com.braincao.mmall.service.ICategoryService;
import com.braincao.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;

    /**
     * 后台：增加类别节点
     * @param parentId
     * @param categoryName
     * @param session
     * @return
     */
    @RequestMapping(value = "add_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse addCategory(@RequestParam(value = "parentId", defaultValue = "0") Integer parentId, String categoryName, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(),"用户未登录，请登录");
        }
        //校验下是否是管理员
        if(iUserService.checkAdminRole(user).isSuccess()) {
            //是管理员，增加我们处理分类的逻辑
            return iCategoryService.addCategory(parentId, categoryName);
        }

        return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
    }

    /**
     * 修改品类名字
     * @param categoryId
     * @param categoryName
     * @param session
     * @return
     */
    @RequestMapping(value = "set_category_name.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setCategoryName(Integer categoryId, String categoryName, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(),"用户未登录，请登录");
        }
        //校验下是否是管理员
        if(iUserService.checkAdminRole(user).isSuccess()) {
            //是管理员，增加我们处理分类的逻辑
            return iCategoryService.updateCategoryName(categoryId, categoryName);
        }
        return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
    }

    /**
     * 后台：获取品类子节点(平级)
     * @param categoryId
     * @param session
     * @return
     */
    @RequestMapping(value = "get_category.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(),"用户未登录，请登录");
        }
        //校验下是否是管理员
        if(iUserService.checkAdminRole(user).isSuccess()) {
            //是管理员，查询子节点的category信息，并且不递归，保持平级
            return iCategoryService.getChildrenParallelCategory(categoryId);
        }

        return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
    }


    /**
     * 获取当前分类id及递归子节点categoryId
     * @param categoryId
     * @param session
     * @return
     */
    @RequestMapping(value = "get_deep_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(),"用户未登录，请登录");
        }
        //校验下是否是管理员
        if(iUserService.checkAdminRole(user).isSuccess()) {
            //是管理员，获取当前分类id及递归子节点categoryId
            return iCategoryService.getCategoryAndDeepChildrenCategory(categoryId);
        }

        return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
    }

}