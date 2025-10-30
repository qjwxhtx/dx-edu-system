package com.edu.auth.dto;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

/**
 *
 *
 * @author hjw
 * @since 2025-10-27
 */
@Getter
public class AuthUser extends User {

    private Long userId;

    private Long tenantId;

    private String systemCode;

    private String email;

    private Integer status;

    private List<String> roles;

    private List<String> permissions;

    public AuthUser(Long userId, Long tenantId, String systemCode, String username, String password, String email, Integer status,
        Collection<? extends GrantedAuthority> authorities, List<String> roles, List<String> permissions) {
        super(username, password, status == 1, true, true, true, authorities);
        this.userId = userId;
        this.tenantId = tenantId;
        this.systemCode = systemCode;
        this.email = email;
        this.status = status;
        this.roles = roles;
        this.permissions = permissions;
    }

}
