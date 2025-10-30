package com.edu.auth.service;

import com.edu.auth.dto.AuthUser;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;

/**
 * 自定义令牌增强器
 * 原理：在标准的OAuth2令牌中添加自定义信息
 *
 * @author hjw
 * @since 2025-10-28
 */
@Component
public class CustomTokenEnhancer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    /**
     * 自定义JWT令牌
     * 原理：在JWT令牌编码过程中添加自定义声明,这个方法会在JWT令牌生成时自动被调用
     */
    @Override
    public void customize(JwtEncodingContext context) {
        // 只处理访问令牌
        if (context.getTokenType().getValue().equals("access_token")) {
            // 获取认证用户信息
            if (context.getPrincipal().getPrincipal() instanceof AuthUser authUser) {

                // 添加自定义声明到JWT
                context.getClaims().claims(claims -> {
                    claims.put("userId", authUser.getUserId());
                    claims.put("tenantId", authUser.getTenantId());
                    claims.put("username", authUser.getUsername());
                    claims.put("email", authUser.getEmail());
                    claims.put("roles", authUser.getRoles());
                    claims.put("permissions", authUser.getPermissions());
                });
            }
        }
    }
}
