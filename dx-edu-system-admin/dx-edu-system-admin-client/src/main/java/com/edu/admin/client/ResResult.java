package com.edu.admin.client;

import com.edu.admin.client.enums.HttpCodeEnum;

import java.io.Serializable;

public class ResResult<T> implements Serializable {
    private Integer code;

    private String msg;

    private T data;

    public ResResult() {
        this.code = HttpCodeEnum.SUCCESS.getCode();
        this.msg = HttpCodeEnum.SUCCESS.getMsg();
    }

    public ResResult(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    public ResResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static ResResult errorResult(int code, String msg) {
        ResResult result = new ResResult();
        return result.error(code, msg);
    }

    public static ResResult okResult() {
        ResResult result = new ResResult();
        return result;
    }

    public static ResResult okResult(int code, String msg) {
        ResResult result = new ResResult();
        return result.ok(code, null, msg);
    }

    public static ResResult okResult(Object data) {
        ResResult result = setAppHttpCodeEnum(HttpCodeEnum.SUCCESS, HttpCodeEnum.SUCCESS.getMsg());
        if (data != null) {
            result.setData(data);
        }
        return result;
    }

    public static ResResult errorResult(HttpCodeEnum enums) {
        return setAppHttpCodeEnum(enums, enums.getMsg());
    }

    public static ResResult errorResult(HttpCodeEnum enums, String msg) {
        return setAppHttpCodeEnum(enums, msg);
    }

    public static ResResult setAppHttpCodeEnum(HttpCodeEnum enums) {
        return okResult(enums.getCode(), enums.getMsg());
    }

    private static ResResult setAppHttpCodeEnum(HttpCodeEnum enums, String msg) {
        return okResult(enums.getCode(), msg);
    }

    public ResResult<?> error(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
        return this;
    }

    public ResResult<?> ok(Integer code, T data) {
        this.code = code;
        this.data = data;
        return this;
    }

    public ResResult<?> ok(Integer code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        return this;
    }

    public ResResult<?> ok(T data) {
        this.data = data;
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
