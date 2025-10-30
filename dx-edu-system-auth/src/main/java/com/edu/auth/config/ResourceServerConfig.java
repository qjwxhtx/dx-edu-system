package com.edu.auth.config;

import com.edu.auth.convert.CustomJwtGrantedAuthoritiesConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 资源服务器配置
 * 原理：保护受保护的资源，验证访问令牌
 *
 * @author hjw
 * @since 2025-10-27
 */
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class ResourceServerConfig {

    /**
     * 资源服务器安全过滤器链
     * 原理：配置受保护资源的访问规则和JWT验证
     */
    @Bean
    public SecurityFilterChain resourceServerFilterChain(HttpSecurity http) throws Exception {
        http
            // 指定资源服务器的路径
            .securityMatcher("/api/**")

            // 授权规则
            .authorizeHttpRequests(authz -> authz.anyRequest().authenticated()  // 所有请求都需要认证
            )

            // OAuth2资源服务器配置
            .oauth2ResourceServer(
                oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())  // 自定义JWT转换
                ))

            // 会话管理
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    /**
     * JWT认证转换器
     * 原理：将JWT声明转换为Spring Security的认证信息
     */
    private Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();

        // 设置自定义的权限转换器
        jwtConverter.setJwtGrantedAuthoritiesConverter(new CustomJwtGrantedAuthoritiesConverter());

        return jwtConverter;
    }
}
