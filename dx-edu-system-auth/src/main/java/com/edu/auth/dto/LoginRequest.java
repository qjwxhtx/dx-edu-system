package com.edu.auth.dto;

import lombok.Data;

/**
 *
 *
 * @author hjw
 * @since 2025-10-27
 */
@Data
public class LoginRequest {
    private String username;
    private String password;
    private String tenantCode;
}
