package com.braincao.mmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

public class Const {

    public static final String CURRENT_USER = "currentUser";

    public static final String EMAIL = "email";

    public static final String USERNAME = "username";

    //这里不用enum，采用轻量级的interface定义常量，是一个很好的技巧，因为interface定义的常量是static final的
    public interface Role{
        int ROLE_CUSTOMER = 0; //普通用户
        int ROLE_ADMIN = 1; //管理员
    }

    public interface Cart{
        int CHECKED = 1; //购物车选中状态
        int UN_CHECKED = 0; //购物车未选中状态

        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";//购买数量与库存比较的判断时，返回的判断结果
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
    }

    public enum ProductStatusEnum{
        ON_SALE("在售", 1),
        OFF_SHELF("下架", 2),
        DELETE("删除", 3);

        private String status;
        private int code;

        ProductStatusEnum(String status, int code) {
            this.status = status;
            this.code = code;
        }

        public String getStatus() {
            return status;
        }

        public int getCode() {
            return code;
        }
    }

    public interface ProductListOrderBy{
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc","price_asc");
    }

}
