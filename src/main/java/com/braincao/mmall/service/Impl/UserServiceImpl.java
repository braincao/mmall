package com.braincao.mmall.service.Impl;

import com.braincao.mmall.common.Const;
import com.braincao.mmall.common.ServerResponse;
import com.braincao.mmall.common.TokenCache;
import com.braincao.mmall.dao.UserMapper;
import com.braincao.mmall.pojo.User;
import com.braincao.mmall.service.IUserService;
import com.braincao.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("iUserService")
//定义iUserService名字，就可以在controller层自动完成注入
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    //登录
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        //用户不存在
        if(resultCount == 0){
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        //密码登录，比较的是md5密码
        String md5Password = MD5Util.MD5EncodeUtf8(password);

        //用户登录
        User user = userMapper.selectLogin(username, md5Password);
        if(user==null){
            return ServerResponse.createByErrorMessage("密码错误");
        }

        //密码置为空
        user.setPassword(StringUtils.EMPTY);

        return  ServerResponse.createBySuccessMessageData("登陆成功", user);
    }

    //注册
    public ServerResponse<String> register(User user){

        //分别校验用户输入的用户名、邮箱
        ServerResponse validResponse = this.checkValid(user.getUsername(), Const.USERNAME);
        if(!validResponse.isSuccess()){
            return validResponse;
        }
        validResponse  = this.checkValid(user.getUsername(), Const.EMAIL);
        if(!validResponse.isSuccess()){
            return validResponse;
        }

        //将注册用户权限设置为普通用户
        user.setRole(Const.Role.ROLE_CUSTOMER);

        //MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int resultCount = userMapper.insert(user);

        //注册失败
        if(resultCount == 0){
            return ServerResponse.createByErrorMessage("注册失败");
        }

        return ServerResponse.createBySuccessMessage("注册成功");
    }

    //校验用户注册信息是否有效
    public ServerResponse<String> checkValid(String str, String type){
        if(StringUtils.isNoneBlank(type)){
            //校验用户名
            if(Const.USERNAME.equals(type)){
                int resultCount = userMapper.checkUsername(str);
                if(resultCount > 0){
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }

            //校验邮箱
            if(Const.EMAIL.equals(type)){
                int resultCount = userMapper.checkEmail(str);
                if(resultCount > 0){
                    return ServerResponse.createByErrorMessage("邮箱已存在");
                }
            }
        }
        else{
            return ServerResponse.createByErrorMessage("参数错误");
        }

        return ServerResponse.createBySuccessMessage("校验成功");
    }

    //忘记密码，返回用户设置的提示问题
    public ServerResponse<String> selectQuestion(String username){
        ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
        //当用户不存在时
        if(validResponse.isSuccess()){
            return ServerResponse.createByErrorMessage("用户不存在");
        }

        String question = userMapper.selectQuestionByUsername(username);
        if(StringUtils.isNoneBlank(question)){
            return ServerResponse.createBySuccessData(question);
        }

        return ServerResponse.createByErrorMessage("找回密码的问题未设置");
    }

    //忘记密码，检查答案
    public ServerResponse<String> checkAnswer(String username, String question, String answer){
        int resultCount = userMapper.checkAnswer(username,question,answer);
        if(resultCount>0){
            //说明问题及问题答案是这个用户的，并且是这正确的
            //在TokenCache中缓存一个token，用于后续调用判断
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username, forgetToken);
            return ServerResponse.createBySuccessData(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题的答案错误");
    }

    //忘记密码，重置密码
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken){
        if(StringUtils.isBlank(forgetToken)){
            return ServerResponse.createByErrorMessage("参数错误，token需要传递");
        }
        ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
        if(validResponse.isSuccess()){
            return ServerResponse.createByErrorMessage("用户不存在");
        }

        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        if(StringUtils.isBlank(forgetToken)){
            return ServerResponse.createByErrorMessage("token无效或过期");
        }

        if(StringUtils.equals(token,forgetToken)){
            String md5PasswordNew = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount = userMapper.updatePasswordByUsername(username,md5PasswordNew);
            if(rowCount>0){
                return ServerResponse.createBySuccessMessage("修改密码成功");
            }
        }
        else{
            return ServerResponse.createByErrorMessage("token错误，请重新获取重置密码的token");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }

    //登录中状态重置密码
    public ServerResponse<String> resetPassword(User user, String passwordOld, String passwordNew){
        //先进行旧密码校验
        int resultCount = userMapper.checkPassword(user.getId(), MD5Util.MD5EncodeUtf8(passwordOld));
        if(resultCount==0){
            return ServerResponse.createByErrorMessage("旧密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        resultCount = userMapper.updateByPrimaryKeySelective(user);
        if(resultCount>0){
            return ServerResponse.createBySuccessMessage("重置密码成功");
        }
        return ServerResponse.createByErrorMessage("重置密码失败");
    }

    //登录状态更新个人信息
    public ServerResponse<User> updateInformation(User user){
        //更新的邮箱需要校验，不能是别的用户用过的
        int resultCount = userMapper.checkEmailByUserId(user.getId(), user.getEmail());
        if(resultCount>0){
            return ServerResponse.createBySuccessMessageData("邮箱已被别的用户注册，请换其他邮箱", user);
        }
        //username是不能被更新的
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        resultCount = userMapper.updateByPrimaryKeySelective(user);
        if(resultCount>0){
            return ServerResponse.createBySuccessMessageData("更新个人信息成功", updateUser);
        }
        return ServerResponse.createByErrorMessage("更新个人信息失败");
    }

    //获取当前登录用户的详细信息
    public ServerResponse<User> getInformationById(Integer userId){
        User user = userMapper.selectByPrimaryKey(userId);
        if(user==null){
            return ServerResponse.createByErrorMessage("找不到当前用户");
        }
        //把用户密码置空
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccessData(user);
    }

}
