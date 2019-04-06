package com.braincao.mmall.service.Impl;

import com.braincao.mmall.common.ServerResponse;
import com.braincao.mmall.dao.CategoryMapper;
import com.braincao.mmall.pojo.Category;
import com.braincao.mmall.service.ICategoryService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service("iCategoryService")
//定义iUserService名字，就可以在controller层自动完成注入
public class CategoryServiceImpl implements ICategoryService {

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

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

    //获取品类子节点(平级)
    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId){
        if(categoryId==null){
            return ServerResponse.createByErrorMessage("修改品类参数错误");
        }
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if(CollectionUtils.isEmpty(categoryList)){
            logger.info("当前品类无子分类");
        }
        return ServerResponse.createBySuccessData(categoryList);
    }

    //获取当前分类id及递归子节点categoryId
    public ServerResponse<List<Category>> getCategoryAndDeepChildrenCategory(Integer categoryId){
        if(categoryId==null){
            return ServerResponse.createByErrorMessage("修改品类参数错误");
        }
        //采用guava的set，很强大
        Set<Category> categorySet = Sets.newHashSet();
        //递归，找到所有子节点存到categorySet中
        findChildrenCategory(categoryId, categorySet);

        List<Category> categoryList = Lists.newArrayList();

        for(Category categoryItem: categorySet){
            categoryList.add(categoryItem);
        }
        return ServerResponse.createBySuccessData(categoryList);
    }

    //递归算法，获取递归的所有子节点
    private Set<Category> findChildrenCategory(Integer categoryId, Set<Category> categorySet){
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category!=null){
            categorySet.add(category);
        }
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for(Category categoryItem: categoryList){
            findChildrenCategory(categoryItem.getId(), categorySet);
        }
        return categorySet;
    }




}
