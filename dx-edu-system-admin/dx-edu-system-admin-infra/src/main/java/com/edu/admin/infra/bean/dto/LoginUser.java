package com.edu.admin.infra.bean.dto;

import lombok.Data;

import java.util.Set;

/**
 *
 *
 * @author hjw
 * @since 2025-10-29
 */
@Data
public class LoginUser {

    private Long userId;
    private String username;
    private String tenantId;
    private String systemCode;
    private Set<String> permissions;
    private Set<String> roles;
    private String tokenRemainingTime;

}
