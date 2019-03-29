package com.braincao.mmall.dao;

import com.braincao.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    //用户登录时检查用户名是否存在
    int checkUsername(String username);

    //用户登录，返回登录成功的User对象
    User selectLogin(@Param("username") String username, @Param("password") String password);

    //用户注册时校验邮箱是否存在，返回登录成功的User对象
    int checkEmail(String email);

    //忘记密码，返回用户设置的提示问题
    String selectQuestionByUsername(String username);

    //忘记密码，检查用户的问题答案
    int checkAnswer(@Param("username") String username, @Param("question") String question, @Param("answer") String answer);

    //忘记密码，重置密码
    int updatePasswordByUsername(@Param("username") String username, @Param("password") String password);

    //登录中状态重置密码，先进行旧密码校验
    int checkPassword(@Param("userId") Integer userId, @Param("password") String password);

    //登录状态更新个人信息，更新的邮箱需要校验，不能是别的用户用过的
    int checkEmailByUserId(@Param("userId") Integer userId, @Param("email") String email);

}