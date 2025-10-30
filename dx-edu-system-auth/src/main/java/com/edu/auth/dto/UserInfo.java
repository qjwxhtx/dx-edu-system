package com.edu.auth.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 *
 *
 * @author hjw
 * @since 2025-10-27
 */
@Data
@Builder
public class UserInfo {
    private Long userId;
    private String username;
    private String email;
    private Long tenantId;
    private List<String> roles;
    private List<String> permissions;
}
