package com.edu.common.enums;

import lombok.Getter;

/**
 * 状态码枚举类
 *
 * @author hjw
 * @since 2025-10-28
 */
@Getter
public enum ResCodeEnum {

    SUCCESS("0000000", "成功"),

    // 10XXXXX 平台级错误
    AUTH_TOKEN_EXPIRED("1010001", "令牌无效"),
    AUTH_TALENT_NOT_FOUND("1020002", "租户不存在"),

    // 20XXXXX 教务系统项目
    // 2001XXX 用户模块
    USER_NOT_FOUND("2001001", "用户不存在"),
    // 2002XXX 订单模块
    ORDER_CLOSED("2002001", "订单已关闭")
    ;


    private final String code;
    private final String msg;

    ResCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
