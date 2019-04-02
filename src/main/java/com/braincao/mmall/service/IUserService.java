package com.braincao.mmall.service;

import com.braincao.mmall.common.ServerResponse;
import com.braincao.mmall.pojo.User;

public interface IUserService {

    //登录
    ServerResponse<User> login(String username, String password);

    //注册
    ServerResponse<String> register(User user);

    //校验用户注册信息是否有效，便于用户注册时前端实时校验
    ServerResponse<String> checkValid(String str, String type);

    //忘记密码，返回用户设置的提示问题
    ServerResponse<String> selectQuestion(String username);

    //忘记密码，检查用户的答案
    ServerResponse<String> checkAnswer(String username, String question, String answer);

    //忘记密码，重设密码
    ServerResponse<String> forgetResetPassword(String username, String passwordNew, String token);

    //登录中状态重置密码
    ServerResponse<String> resetPassword(User user, String passwordOld, String passwordNew);

    //登录状态更新个人信息
    ServerResponse<User> updateInformation(User user);

    //获取当前登录用户的详细信息
    ServerResponse<User> getInformationById(Integer userId);

    //后台backend校验是否登录用户为管理员
    ServerResponse checkAdminRole(User user);

}
