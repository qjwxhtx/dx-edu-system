package com.edu.auth.dto;

import lombok.Builder;
import lombok.Data;

/**
 *
 *
 * @author hjw
 * @since 2025-10-27
 */
@Data
@Builder
public class ApiResponse<T> {
    private Integer code;
    private String message;
    private T data;
    private Long timestamp;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
            .code(200)
            .message("success")
            .data(data)
            .timestamp(System.currentTimeMillis())
            .build();
    }

    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
            .code(500)
            .message(message)
            .timestamp(System.currentTimeMillis())
            .build();
    }
}
