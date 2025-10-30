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
public class LoginResponse {
    private String accessToken;
    private String tokenType;
    private Long expiresIn;
    private UserInfo userInfo;
}
