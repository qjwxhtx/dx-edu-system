package com.edu.admin.infra.filter;

import com.edu.admin.infra.bean.dto.LoginUser;
import com.edu.admin.infra.context.SecurityContext;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 租户过滤器
 *
 * @author hjw
 * @since 2025-10-29
 */
@Slf4j
@Component
public class TenantFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
        FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        try {
            // 从请求头获取用户信息
            String userId = httpRequest.getHeader("X-User-Id");
            String username = httpRequest.getHeader("X-Username");
            String tenantId = httpRequest.getHeader("X-Tenant-Id");
            String systemCode = httpRequest.getHeader("X-System-Code");
            String roleHeader = httpRequest.getHeader("X-Roles");
            String permissionsHeader = httpRequest.getHeader("X-Permissions");
            String tokenRemainingTime = httpRequest.getHeader("X-Token-Remaining-Time");

            if (userId != null && tenantId != null) {
                LoginUser loginUser = new LoginUser();
                loginUser.setUserId(Long.valueOf(userId));
                loginUser.setUsername(username);
                loginUser.setTenantId(tenantId);
                loginUser.setSystemCode(systemCode);

                //解析角色字符串
                Set<String> roles = new HashSet<>();
                if (roleHeader != null && !roleHeader.isEmpty()) {
                    roles.addAll(Arrays.asList(roleHeader.split(",")));
                }
                loginUser.setRoles(roles);
                // 解析权限字符串
                Set<String> permissions = new HashSet<>();
                if (permissionsHeader != null && !permissionsHeader.isEmpty()) {
                    permissions.addAll(Arrays.asList(permissionsHeader.split(",")));
                }
                loginUser.setPermissions(permissions);
                loginUser.setTokenRemainingTime(tokenRemainingTime);

                SecurityContext.setUser(loginUser);
            }

            chain.doFilter(request, response);
        } finally {
            SecurityContext.clear();
        }
    }

}
