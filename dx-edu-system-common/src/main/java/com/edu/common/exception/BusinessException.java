package com.edu.common.exception;

import com.edu.common.enums.ResCodeEnum;
import lombok.Getter;

/**
 * 业务异常类
 *
 * @author hjw
 * @since 2025-10-28
 */
@Getter
public class BusinessException extends RuntimeException {

    private final String code;

    private final String message;

    public BusinessException(ResCodeEnum resCodeEnum) {
        super(resCodeEnum.getMsg());
        this.code = resCodeEnum.getCode();
        this.message = resCodeEnum.getMsg();
    }

}
