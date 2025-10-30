package com.edu.gateway.filter;

import com.edu.gateway.jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 认证过滤器
 *
 * @author hjw
 * @since 2025-10-27
 */
@Slf4j
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    private final JwtTokenProvider jwtTokenProvider;

    private final RedisTemplate<String, Object> redisTemplate;

    private final ObjectMapper objectMapper;

    private final AntPathMatcher pathMatcher;

    private static final List<String> WHITE_LIST =
        Arrays.asList("/auth/api/login", "/auth/actuator/health", "/auth/oauth2/token");

    private static final String TOKEN_HEADER = "Authorization";

    private static final String TOKEN_PREFIX = "Bearer ";

    @Autowired
    public AuthFilter(JwtTokenProvider jwtTokenProvider, RedisTemplate<String, Object> redisTemplate,
        ObjectMapper objectMapper) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.pathMatcher = new AntPathMatcher();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        String method = request.getMethod().name();

        log.debug("Processing request: {} {}", method, path);

        // 检查白名单
        if (isWhiteList(path)) {
            log.debug("White list access granted for: {}", path);
            return chain.filter(exchange);
        }

        // 获取令牌
        String token = resolveToken(request);
        if (!StringUtils.hasText(token)) {
            log.warn("Missing authentication token for: {}", path);
            return unauthorizedResponse(exchange, "Missing authentication token", "AUTH_TOKEN_MISSING");
        }

        try {
            // 验证令牌有效性
            if (!jwtTokenProvider.validateToken(token)) {
                log.warn("Invalid token for: {}", path);
                return unauthorizedResponse(exchange, "Invalid or expired token", "AUTH_TOKEN_INVALID");
            }

            // 检查令牌黑名单
            if (isTokenBlacklisted(token)) {
                log.warn("Token is blacklisted for: {}", path);
                return unauthorizedResponse(exchange, "Token has been revoked", "AUTH_TOKEN_REVOKED");
            }

            // 提取用户信息
            String username = jwtTokenProvider.getUsernameFromToken(token);
            Long userId = jwtTokenProvider.getUserIdFromToken(token);
            Long tenantId = jwtTokenProvider.getTenantIdFromToken(token);
            String systemCode = jwtTokenProvider.getSystemCodeFromToken(token);
            List<String> roles = jwtTokenProvider.getRolesFromToken(token);
            List<String> permissions = jwtTokenProvider.getPermissionsFromToken(token);
            long remainingTime = jwtTokenProvider.getRemainingTime(token);

            log.debug("User authenticated: username={}, userId={}, tenantId={}, roles={}", username, userId, tenantId,
                roles);

            // 构建增强的请求头
            ServerHttpRequest mutatedRequest =
                buildEnhancedRequest(request, userId, username, tenantId, systemCode, roles, permissions, remainingTime, token);

            // 传递到下一个过滤器
            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        } catch (Exception e) {
            log.error("Authentication processing failed for {}: {}", path, e.getMessage(), e);
            return unauthorizedResponse(exchange, "Authentication processing failed: " + e.getMessage(),
                "AUTH_PROCESSING_ERROR");
        }
    }

    /**
     * 解析令牌
     */
    private String resolveToken(ServerHttpRequest request) {
        // 1. 从Authorization头获取
        String bearerToken = request.getHeaders().getFirst(TOKEN_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }

        // 2. 从查询参数获取
        String tokenParam = request.getQueryParams().getFirst("access_token");
        if (StringUtils.hasText(tokenParam)) {
            return tokenParam;
        }

        // 3. 从Cookie获取
        /* 如果需要从Cookie获取，可以在这里实现 */

        return null;
    }

    /**
     * 检查白名单
     */
    private boolean isWhiteList(String path) {
        return WHITE_LIST.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    /**
     * 检查令牌黑名单
     */
    private boolean isTokenBlacklisted(String token) {
        return redisTemplate.hasKey("token:blacklist:" + token);
    }

    /**
     * 构建增强的请求头
     */
    private ServerHttpRequest buildEnhancedRequest(ServerHttpRequest request, Long userId, String username,
        Long tenantId, String systemCode, List<String> roles, List<String> permissions, long remainingTime, String token) {
        return request.mutate()
            .header("X-User-Id", String.valueOf(userId))
            .header("X-Username", username)
            .header("X-Tenant-Id", String.valueOf(tenantId))
            .header("X-System-Code", systemCode)
            .header("X-Roles", convertListToString(roles))
            .header("X-Permissions", convertListToString(permissions))
            .header("X-Token-Remaining-Time", String.valueOf(remainingTime))
            .header("X-Authenticated", "true")
            .build();
    }

    /**
     * 列表转字符串
     */
    private String convertListToString(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        return String.join(",", list);
    }

    /**
     * 未授权响应
     */
    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String message, String errorCode) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 401);
        result.put("message", message);
        result.put("errorCode", errorCode);
        result.put("path", exchange.getRequest().getURI().getPath());
        result.put("timestamp", System.currentTimeMillis());

        try {
            String jsonResponse = objectMapper.writeValueAsString(result);
            DataBuffer buffer = response.bufferFactory().wrap(jsonResponse.getBytes(StandardCharsets.UTF_8));
            return response.writeWith(Mono.just(buffer));
        } catch (Exception e) {
            log.error("Failed to write error response", e);
            return response.setComplete();
        }
    }

    @Override
    public int getOrder() {
        // 最高优先级，确保在其他过滤器之前执行
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
