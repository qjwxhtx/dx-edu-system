package com.edu.common.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Method;

/**
 * 请求日志切面 - 简洁格式
 */
@Aspect
@Component
@Slf4j
public class SimpleRequestLogAspect {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 定义切点：所有Controller包下的方法
     */
    @Pointcut("execution(* com.edu.*.adapter.web..*.*(..)) || " + "execution(* com.edu.*.adapter.wap..*.*(..)) || "
        + "execution(* com.edu.*.adapter.mobile..*.*(..))")
    public void controllerPointcut() {
    }

    /**
     * 环绕通知
     */
    @Around("controllerPointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;

        Object result = null;
        Throwable exception = null;

        try {
            // 执行目标方法
            result = joinPoint.proceed();
            return result;
        } catch (Throwable throwable) {
            exception = throwable;
            throw throwable;
        } finally {
            // 记录响应日志
            long endTime = System.currentTimeMillis();
            logRequestAndResponse(joinPoint, request, result, exception, endTime - startTime);
        }
    }

    private void logRequestAndResponse(ProceedingJoinPoint joinPoint, HttpServletRequest request, Object result,
        Throwable exception, long executionTime) {
        try {
            StringBuilder logContent = new StringBuilder();
            logContent.append("\n==========================   start    ==========================");
            // 请求IP
            if (request != null) {
                logContent.append("\ntraceId : ").append(request.getHeader("traceId")).append("\n");
                logContent.append("IP      : ").append(getClientIp(request)).append("\n");
            } else {
                logContent.append("IP      : N/A\n");
            }
            // URL
            if (request != null) {
                String url = request.getRequestURL().toString();
                String queryString = request.getQueryString();
                if (queryString != null) {
                    url += "?" + queryString;
                }
                logContent.append("URL     : ").append(url).append("\n");
            } else {
                logContent.append("URL     : N/A\n");
            }
            // HTTP Method
            if (request != null) {
                logContent.append("HTTP Method     : ").append(request.getMethod()).append("\n");
            } else {
                logContent.append("HTTP Method     : N/A\n");
            }
            logContent.append("Class Method    : ").append(getClassMethodInfo(joinPoint)).append("\n");
            logContent.append("Request Args    : ").append(getRequestParams(joinPoint)).append("\n");
            // 如果有异常，显示异常信息，不显示Response Args
            if (exception != null) {
                logContent.append("========================== Exception ==========================\n");
                logContent.append(exception.getClass().getName())
                    .append(": ")
                    .append(exception.getMessage())
                    .append("\n");
                log.error(logContent.toString());
            } else {
                logContent.append("Response Args   : ").append(getResponseResult(result)).append("\n");
                logContent.append("Time-Consuming  : ").append(executionTime).append("ms\n");
                logContent.append("==========================   End    ==========================");
                log.info(logContent.toString());
            }
        } catch (Exception e) {
            log.warn("记录请求日志失败: {}", e.getMessage());
        }
    }

    /**
     * 获取类方法信息（包含文件名和行号）
     */
    private String getClassMethodInfo(ProceedingJoinPoint joinPoint) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            Class<?> targetClass = joinPoint.getTarget().getClass();

            // 获取方法所在的类名和方法名
            String className = targetClass.getName();
            String methodName = method.getName();
            String fileName = targetClass.getSimpleName() + ".java";

            // 注意：在Java中无法直接获取行号，这里使用固定格式
            // 实际行号需要编译器支持调试信息，这里用":行号"占位
            return className + "." + methodName + "(" + fileName + ":" + "行号)";

        } catch (Exception e) {
            return "UnknownMethod(UnknownFile.java:0)";
        }
    }

    /**
     * 获取请求参数
     */
    private String getRequestParams(ProceedingJoinPoint joinPoint) {
        try {
            Object[] args = joinPoint.getArgs();
            if (args == null || args.length == 0) {
                return "{}";
            }
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            String[] parameterNames = signature.getParameterNames();
            // 如果是单个参数，直接显示
            if (args.length == 1) {
                Object arg = args[0];
                if (isSpecialType(arg)) {
                    return "{}";
                }
                return objectMapper.writeValueAsString(arg);
            }
            // 多个参数，构建对象
            StringBuilder paramsBuilder = new StringBuilder();
            paramsBuilder.append("{");
            for (int i = 0; i < args.length; i++) {
                if (isSpecialType(args[i])) {
                    continue;
                }
                if (paramsBuilder.length() > 1) {
                    paramsBuilder.append(", ");
                }
                String paramName = parameterNames != null && i < parameterNames.length ? parameterNames[i] : "arg" + i;
                if (args[i] == null) {
                    paramsBuilder.append("\"").append(paramName).append("\": null");
                } else {
                    paramsBuilder.append("\"")
                        .append(paramName)
                        .append("\": ")
                        .append(objectMapper.writeValueAsString(args[i]));
                }
            }
            paramsBuilder.append("}");

            return paramsBuilder.toString();

        } catch (Exception e) {
            return "{\"error\": \"参数序列化失败\"}";
        }
    }

    /**
     * 判断是否为特殊类型（不需要记录的类型）
     */
    private boolean isSpecialType(Object arg) {
        return arg instanceof HttpServletRequest || arg instanceof HttpServletResponse || arg instanceof MultipartFile;
    }

    /**
     * 获取响应结果
     */
    private String getResponseResult(Object result) {
        if (result == null) {
            return "null";
        }

        try {
            // 如果是字符串，直接返回
            if (result instanceof String) {
                return (String) result;
            }

            // 对于其他对象，尝试JSON序列化
            String jsonResult = objectMapper.writeValueAsString(result);
            // 如果JSON太长，可以截断（可选）
            if (jsonResult.length() > 500) {
                return jsonResult.substring(0, 500) + "...(truncated)";
            }
            return jsonResult;

        } catch (Exception e) {
            return result.toString();
        }
    }

    /**
     * 获取客户端IP
     */
    private String getClientIp(HttpServletRequest request) {
        String[] headers =
            {"X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"};

        for (String header : headers) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                if (ip.contains(",")) {
                    ip = ip.split(",")[0];
                }
                return ip;
            }
        }
        return request.getRemoteAddr();
    }
}
