package com.braincao.mmall.service.Impl;

import com.braincao.mmall.common.ServerResponse;
import com.braincao.mmall.dao.ShippingMapper;
import com.braincao.mmall.pojo.Shipping;
import com.braincao.mmall.service.IShippingService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    //前台购物车添加商品
    @Override
    public ServerResponse addShipping(Integer userId, Shipping shipping){
        shipping.setUserId(userId);

        int row = shippingMapper.insert(shipping);
        if(row>0){
            Map map = new HashMap();
            map.put("shippingId", shipping.getId());
            return ServerResponse.createBySuccessMessageData("新建地址成功", map);
        }
        return ServerResponse.createByErrorMessage("添加地址失败");
    }

    //收货地址删除地址，这里要避免横向越权的问题
    @Override
    public ServerResponse<String> delShipping(Integer userId, Integer shippingId){
        int row = shippingMapper.deleteByShippingIdUserId(shippingId, userId);
        if(row>0){
            return ServerResponse.createBySuccessMessage("删除地址成功");
        }
        return ServerResponse.createByErrorMessage("删除地址失败");
    }

    //登录状态更新地址
    @Override
    public ServerResponse updateShipping(Integer userId, Shipping shipping){
        shipping.setUserId(userId);
        int row = shippingMapper.updateByUserId(shipping);
        if(row>0){
            return ServerResponse.createBySuccessMessage("更新地址成功");
        }
        return ServerResponse.createByErrorMessage("更新地址失败");
    }

    @Override
    public ServerResponse<Shipping> selectShipping(Integer userId, Integer shippingId){
        Shipping shipping = shippingMapper.selectByShippingIdUserId(shippingId,userId);
        if(shipping!=null){
            return ServerResponse.createBySuccessMessageData("查询地址成功", shipping);
        }
        return ServerResponse.createByErrorMessage("无法查询到该地址");
    }

    @Override
    //产品搜索:结果进行分页
    public ServerResponse<PageInfo> searchProductListByIdOrName(Integer userId, int pageNum, int pageSize){
        //1.pageHelper--startPage
        PageHelper.startPage(pageNum, pageSize);

        //2.填充自己的sql查询逻辑
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);

        //3.pageHelper--收尾
        PageInfo pageResult = new PageInfo(shippingList);

        return ServerResponse.createBySuccessData(pageResult);
    }

}
