package com.edu.admin.client.enums;

/**
 * 响应码枚举类
 *
 * @author zhouqingrui
 * @since 2025-10-15
 */
public enum HttpCodeEnum {
    SUCCESS(200, "操作成功");
    int code;
    String msg;

    HttpCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
