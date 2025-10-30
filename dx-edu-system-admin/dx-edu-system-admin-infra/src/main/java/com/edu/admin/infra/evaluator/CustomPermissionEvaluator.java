package com.edu.admin.infra.evaluator;

import com.edu.admin.infra.bean.dto.LoginUser;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 自定义权限评估
 *
 * @author hjw
 * @since 2025-10-29
 */
@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        // 检查用户是否有指定权限
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        return loginUser.getPermissions().contains(permission.toString());
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
        Object permission) {
        // 基于ID和类型的权限检查
        return hasPermission(authentication, null, permission);
    }
}
