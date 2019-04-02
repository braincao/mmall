package com.braincao.mmall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * 用泛型定义一个高可用服务器响应类，封装具体的响应T data，序列化后传给前端
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
//序列化json的时候，如果是null的对象，key也会消失。e.g.当只想返回status一个参数给前端时，msg/data为null不传给前端
public class ServerResponse<T> implements Serializable {

    private int status;
    private String msg;
    private T data;

    private ServerResponse(int status) {
        this.status = status;
    }

    private ServerResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    private ServerResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }

    private ServerResponse(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    @JsonIgnore
    //传给前端时，使之不在json序列化结果当中
    public boolean isSuccess(){
        return this.status == ResponseCode.SUCCESS.getStatus();
    }

    //SUCCESS
    public static <T> ServerResponse<T> createBySuccess(){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getStatus());

    }

    public static <T> ServerResponse<T> createBySuccessMessage(String msg){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getStatus(), msg);
    }

    public static <T> ServerResponse<T> createBySuccessData(T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getStatus(), data);
    }

    public static <T> ServerResponse<T> createBySuccessMessageData(String msg, T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getStatus(), msg, data);
    }

    //ERROR
    public static <T> ServerResponse<T> createByError(){
        return new ServerResponse<T>(ResponseCode.ERROR.getStatus(), ResponseCode.ERROR.getMsg() );
    }

    public static <T> ServerResponse<T>  createByErrorMessage(String errorMsg){
        return new ServerResponse<T>(ResponseCode.ERROR.getStatus(), errorMsg);
    }

    public static <T> ServerResponse<T> createByErrorCodeMessage(int errorStatus, String errorMsg){
        return new ServerResponse<T>(errorStatus, errorMsg);
    }

}
