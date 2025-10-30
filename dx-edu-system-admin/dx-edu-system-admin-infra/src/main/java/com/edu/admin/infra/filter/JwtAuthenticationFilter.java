package com.edu.admin.infra.filter;

import com.edu.admin.infra.bean.dto.LoginUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *  JWT认证过滤器
 *
 * @author hjw
 * @since 2025-10-29
 */
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        try {
            // 1. 检查是否是公开路径
            if (isPublicPath(request.getServletPath())) {
                filterChain.doFilter(request, response);
                return;
            }

            // 2. 从请求头获取用户信息（由网关添加）
            String userId = request.getHeader("X-User-Id");
            String username = request.getHeader("X-Username");
            String tenantId = request.getHeader("X-Tenant-Id");
            String systemCode = request.getHeader("X-System-Code");
            String roleHeader = request.getHeader("X-Roles");
            String permissionsHeader = request.getHeader("X-Permissions");
            String tokenRemainingTime = request.getHeader("X-Token-Remaining-Time");

            log.debug("从请求头解析用户信息: userId={}, username={}, tenantId={}, systemCode={}", userId, username,
                tenantId, systemCode);

            // 3. 验证必要的信息是否存在
            if (userId == null || username == null) {
                log.warn("请求头中缺少用户认证信息");
                filterChain.doFilter(request, response);
                return;
            }

            // 4. 构建LoginUser对象
            LoginUser loginUser = buildLoginUser(userId, username, tenantId, systemCode, roleHeader, permissionsHeader,
                tokenRemainingTime);

            // 5. 创建Authentication对象
            Authentication authentication = createAuthentication(loginUser);

            // 6. 设置到SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.debug("安全上下文设置成功: {}", authentication.getName());

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("JWT认证过滤器异常", e);
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
        } finally {
            // 清理安全上下文，防止内存泄漏
            SecurityContextHolder.clearContext();
        }
    }

    private boolean isPublicPath(String path) {
        return path.startsWith("/public/") || path.startsWith("/actuator/health") || path.equals("/error");
    }

    private LoginUser buildLoginUser(String userId, String username, String tenantId, String systemCode,
        String roleHeader, String permissionsHeader, String tokenRemainingTime) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(Long.valueOf(userId));
        loginUser.setUsername(username);
        loginUser.setTenantId(tenantId != null ? tenantId : "default-tenant");
        loginUser.setSystemCode(systemCode != null ? systemCode : "default-system");

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
        return loginUser;
    }

    private Authentication createAuthentication(LoginUser loginUser) {
        // 转换权限为GrantedAuthority
        List<GrantedAuthority> authorities =
            loginUser.getPermissions().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        // 创建Spring Security 6风格的认证对象
        return UsernamePasswordAuthenticationToken.authenticated(loginUser,     // principal - 用户信息
            null,          // credentials - 密码（设为null）
            authorities    // authorities - 权限列表
        );
    }
}
