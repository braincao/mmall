package com.braincao.mmall.service;

import com.braincao.mmall.common.ServerResponse;

public interface ICategoryService {

    //后台增加节点
    ServerResponse addCategory(Integer parentId, String categoryName);

    //修改品类名字
    ServerResponse updateCategoryName(Integer categoryId, String categoryName);

}
