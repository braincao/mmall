package com.braincao.mmall.service.Impl;

import com.braincao.mmall.common.ServerResponse;
import com.braincao.mmall.dao.CategoryMapper;
import com.braincao.mmall.pojo.Category;
import com.braincao.mmall.service.ICategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("iCategoryService")
//定义iUserService名字，就可以在controller层自动完成注入
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    //后台增加节点
    @Override
    public ServerResponse addCategory(Integer parentId, String categoryName){
        if(parentId==null || StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("添加品类参数错误");
        }

        Category category = new Category();
        category.setParentId(parentId);
        category.setName(categoryName);
        category.setStatus(true);

        int resultCount = categoryMapper.insert(category);
        if(resultCount>0){
            return ServerResponse.createBySuccessMessage("添加品类成功");
        }
        return ServerResponse.createByErrorMessage("添加品类失败");
    }

    //修改品类名字
    public ServerResponse updateCategoryName(Integer categoryId, String categoryName){
        if(categoryId==null || StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("修改品类参数错误");
        }

        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);

        int resultCount = categoryMapper.updateByPrimaryKeySelective(category);
        if(resultCount>0){
            return ServerResponse.createBySuccessMessage("修改品类名字成功");
        }
        return ServerResponse.createByErrorMessage("修改品类名字失败");
    }

}
