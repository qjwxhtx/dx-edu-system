package com.edu.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.util.Map;

/**
 * Spring Security 核心配置类
 * 原理：配置认证和授权规则，定义安全过滤器链
 *
 * @author hjw
 * @since 2025-10-27
 */
@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final OAuth2AuthorizationService authorizationService;
    private final OAuth2AuthorizationConsentService authorizationConsentService;

    /**
     * OAuth2 授权服务器安全过滤器链
     * 专门处理 /oauth2/** 路径
     * Order(1): 最高优先级，先处理OAuth2端点
     */
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        log.info("Configuring OAuth2 Authorization Server security filter chain...");
        // 应用默认的 OAuth2 授权服务器配置
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        // 配置授权服务器使用数据库存储
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
            .authorizationService(authorizationService)
            .authorizationConsentService(authorizationConsentService);

        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class).oidc(Customizer.withDefaults()) // 启用 OIDC
            .withObjectPostProcessor(new ObjectPostProcessor<Object>() {
                @Override
                public <O> O postProcess(O object) {
                    // 可以在这里进行后处理
                    return object;
                }
            });

        return http.build();
    }

    /**
     * 认证端点安全过滤器链
     * 处理自定义认证端点（/auth/**）和监控端点
     * Order(2): 第二优先级
     */
    @Bean
    @Order(2)
    public SecurityFilterChain authEndpointsSecurityFilterChain(HttpSecurity http) throws Exception {
        log.info("Configuring authentication endpoints security filter chain...");

        return http
            // 只处理认证端点和监控端点
            .securityMatcher("/api/**", "/actuator/**").authorizeHttpRequests(auths -> auths
                // 认证相关公开端点
                .requestMatchers("/api/login", "/api/register", "/api/refresh", "/api/validate").permitAll()

                // 监控端点
                .requestMatchers("/actuator/health", "/actuator/info").permitAll()

                // 其他认证端点需要认证
                .anyRequest().authenticated())

            // 会话管理 - 无状态
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // CSRF禁用 - REST API不需要
            .csrf(AbstractHttpConfigurer::disable)

            // CORS配置
            .cors(Customizer.withDefaults())

            // 异常处理
            .exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint())  // 认证失败处理
                .accessDeniedHandler(accessDeniedHandler())           // 授权失败处理
            ).build();
    }

    /**
     * 默认安全过滤器链
     * 处理其他所有未匹配的请求
     * Order(3): 最低优先级，安全兜底
     */
    @Bean
    @Order(3)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        log.info("Configuring default security filter chain...");

        return http.authorizeHttpRequests(auths -> auths
                // 默认情况下所有请求都需要认证
                .anyRequest().authenticated())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .csrf(AbstractHttpConfigurer::disable)
            .build();
    }

    /**
     * 密码编码器 Bean
     * 原理：使用BCrypt强哈希函数进行密码加密
     * - 每次加密结果不同，但验证时能正确匹配
     * - 自动处理salt生成和存储
     * - 抗彩虹表攻击
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 认证管理器 Bean
     * 原理：Spring Security的核心组件，负责协调认证过程
     * - 管理各种AuthenticationProvider
     * - 处理认证请求
     * - 在OAuth2令牌端点中使用
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * DaoAuthenticationProvider Bean
     * 原理：基于数据库的认证提供者
     * - 使用UserDetailsService加载用户信息
     * - 使用PasswordEncoder验证密码
     * - 处理用户状态检查（启用/禁用、过期等）
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * 认证入口点
     * 原理：当用户访问受保护资源但未认证时调用
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            Map<String, Object> result =
                Map.of("code", 401, "message", "认证失败: " + authException.getMessage(), "path",
                    request.getRequestURI(), "timestamp", System.currentTimeMillis());

            response.getWriter().write(new ObjectMapper().writeValueAsString(result));
        };
    }

    /**
     * 访问拒绝处理器
     * 原理：当用户认证成功但权限不足时调用
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);

            Map<String, Object> result =
                Map.of("code", 403, "message", "权限不足: " + accessDeniedException.getMessage(), "path",
                    request.getRequestURI(), "timestamp", System.currentTimeMillis());

            response.getWriter().write(new ObjectMapper().writeValueAsString(result));
        };
    }
}
