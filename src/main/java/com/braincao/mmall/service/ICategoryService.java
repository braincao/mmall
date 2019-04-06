package com.braincao.mmall.service;

import com.braincao.mmall.common.ServerResponse;
import com.braincao.mmall.pojo.Category;

import java.util.List;

public interface ICategoryService {

    //后台增加节点
    ServerResponse addCategory(Integer parentId, String categoryName);

    //修改品类名字
    ServerResponse updateCategoryName(Integer categoryId, String categoryName);

    //获取品类子节点(平级)
    ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);

    //获取当前分类id及递归子节点categoryId
    ServerResponse<List<Category>> getCategoryAndDeepChildrenCategory(Integer categoryId);

}
