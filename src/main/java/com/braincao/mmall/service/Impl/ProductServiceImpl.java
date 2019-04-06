package com.braincao.mmall.service.Impl;

import com.braincao.mmall.common.ResponseCode;
import com.braincao.mmall.common.ServerResponse;
import com.braincao.mmall.dao.CategoryMapper;
import com.braincao.mmall.dao.ProductMapper;
import com.braincao.mmall.pojo.Category;
import com.braincao.mmall.pojo.Product;
import com.braincao.mmall.service.IProductService;
import com.braincao.mmall.util.DateTimeUtil;
import com.braincao.mmall.util.PropertiesUtil;
import com.braincao.mmall.vo.ProductDetailVo;
import com.braincao.mmall.vo.ProductListVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("iProductService")
class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    //后台 新增OR更新产品
    @Override
    public ServerResponse addOrUpdateProduct(Product product){
        if(product==null){
            return ServerResponse.createByErrorMessage("新增OR更新产品的参数错误");
        }
        //设置产品主图:从sub图片库中提取第一张
        if(StringUtils.isNotBlank(product.getSubImages())){
            String[] subImageArray = product.getSubImages().split(",");
            if(subImageArray.length>0){
                product.setMainImage(subImageArray[0]);
            }
        }
        //该产品有id，数据库中已存在该产品，则根据该id更新该产品
        if(product.getId()!=null){
            int rowCount = productMapper.updateByPrimaryKey(product);
            if(rowCount>0){
                return ServerResponse.createBySuccessMessage("更新产品成功");
            }
            return ServerResponse.createByErrorMessage("更新产品失败");
        }
        else{
            //数据库中不存在该产品，增加该产品
            int rowCount = productMapper.insert(product);
            if(rowCount>0){
                return ServerResponse.createBySuccessMessage("新增产品成功");
            }
            return ServerResponse.createByErrorMessage("新增产品失败");
        }
    }

    //后台 产品上下架
    @Override
    public ServerResponse<String> setSaleStatus(Integer productId, Integer status){
        if(productId==null || status==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getStatus(),ResponseCode.ILLEGAL_ARGUMENT.getMsg());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        //该产品有id，数据库中已存在该产品，则根据该id更新该产品
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if(rowCount>0){
            return ServerResponse.createBySuccessMessage("产品上下架成功");
        }
        return ServerResponse.createByErrorMessage("产品上下架失败");
    }

    //后台 产品详情
    @Override
    public ServerResponse<ProductDetailVo> detail(Integer productId){
        if(productId==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getStatus(),ResponseCode.ILLEGAL_ARGUMENT.getMsg());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product==null){
            return ServerResponse.createByErrorMessage("后台查询产品详情失败，产品已下架或者删除");
        }
        //这里需要创建VO对象--value object
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);

        return ServerResponse.createBySuccessData(productDetailVo);
    }

    private ProductDetailVo assembleProductDetailVo(Product product){
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setName(product.getName());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setStock(product.getStock());
        productDetailVo.setStatus(product.getStatus());

        //parentCategoryId
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if(category==null){
            productDetailVo.setCategoryId(0);//默认根节点
        }else{
            productDetailVo.setCategoryId(category.getParentId());//默认根节点
        }

        //imageHost
        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.httpURL.prefix", "http://img.happymmall.com/"));

        //createTime、updateTime在数据库中是毫秒数，为了便于阅读，创建一个工具类DateTimeUtil
        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));

        return productDetailVo;
    }

    //后台 获取产品list。采用mybatis pageHelper分页
    //1.pageHelper--startPage
    //2.填充自己的sql查询逻辑
    //3.pageHelper--收尾
    @Override
    public ServerResponse<PageInfo> getProductList(int pageNum, int pageSize) {
        //1.pageHelper--startPage
        PageHelper.startPage(pageNum, pageSize);

        //2.填充自己的sql查询逻辑
        List<Product> productList = productMapper.selectList();
        //包装一下,变成vo对象
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for(Product productItem: productList){
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }

        //3.pageHelper--收尾
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVoList);

        return ServerResponse.createBySuccessData(pageResult);
    }

    private ProductListVo assembleProductListVo(Product product){
        ProductListVo productListVo = new productListVo();
        productListVo.setId(product.getId());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setName(product.getName());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setMainImage(product.getMainImage());
        productListVo.setPrice(product.getPrice());
        productListVo.setStatus(product.getStatus());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.httpURL.prefix", "http://img.happymmall.com/"));
        return productListVo;
    }

    //产品搜索:通过productId、productName等搜索，且结果进行分页
    public ServerResponse<PageInfo> searchProductListByIdOrName(String productName, Integer productId, int pageNum, int pageSize){
        //1.pageHelper--startPage
        PageHelper.startPage(pageNum, pageSize);

        //2.填充自己的sql查询逻辑
        List<Product> productList = productMapper.searchProductListByIdOrName(productName, productId);
        //包装一下,变成vo对象
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for(Product productItem: productList){
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }

        //3.pageHelper--收尾
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVoList);

        return ServerResponse.createBySuccessData(pageResult);
    }

}
