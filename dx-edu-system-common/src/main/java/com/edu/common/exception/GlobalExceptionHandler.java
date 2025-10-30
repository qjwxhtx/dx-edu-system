package com.edu.common.exception;

import com.edu.common.pojo.ResResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 全局异常处理器
 *
 * @author hjw
 * @since 2025-10-28
 */
@Slf4j
@ControllerAdvice
@ConditionalOnExpression("${global.exceptionHandler.enabled:true}")
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResResult handleBusinessError(BusinessException ex) {
        return ResResult.errorResult(ex.getCode(), ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResResult handleGeneralException(Exception ex) {
        log.error(ex.getMessage());
        return ResResult.errorResult("500", "系统错误，请联系管理员！");
    }

}
