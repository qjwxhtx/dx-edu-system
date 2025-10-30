package com.edu.gateway.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * JWT工具类
 *
 * @author hjw
 * @since 2025-10-27
 */
@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret:mySuperSecretKeyForJWTGenerationInSpringCloudGateway2024WithHMAC256}")
    private String jwtSecret;

    @Value("${jwt.expiration:3600000}")
    private long jwtExpiration;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        // 验证配置
        if (jwtSecret == null || jwtSecret.trim().isEmpty()) {
            throw new IllegalStateException("JWT secret key must not be null or empty");
        }

        if (jwtSecret.length() < 32) {
            log.warn("JWT secret key is only {} characters long. For security, recommend at least 32 characters.",
                jwtSecret.length());
        }

        // 初始化密钥
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        log.info("JwtTokenProvider initialized successfully. Secret key length: {}", jwtSecret.length());
    }

    /**
     * 验证JWT令牌有效性
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("JWT token has expired: {}", e.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            log.warn("Invalid JWT token format: {}", e.getMessage());
            return false;
        } catch (SecurityException e) {
            log.warn("JWT signature validation failed: {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            log.warn("JWT token is empty or null: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.warn("JWT token validation failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 从令牌中解析声明信息
     */
    public Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        } catch (Exception e) {
            log.error("Failed to parse JWT claims: {}", e.getMessage());
            throw new RuntimeException("Failed to parse JWT claims: " + e.getMessage(), e);
        }
    }

    /**
     * 获取用户名
     */
    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    /**
     * 获取用户ID
     */
    public Long getUserIdFromToken(String token) {
        return getClaimsFromToken(token).get("userId", Long.class);
    }

    /**
     * 获取租户ID
     */
    public Long getTenantIdFromToken(String token) {
        return getClaimsFromToken(token).get("tenantId", Long.class);
    }

    /**
     * 获取系统编码
     */
    public String getSystemCodeFromToken(String token) {
        return getClaimsFromToken(token).get("systemCode", String.class);
    }

    /**
     * 获取角色列表
     */
    @SuppressWarnings("unchecked")
    public List<String> getRolesFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        Object rolesObj = claims.get("roles");
        if (rolesObj instanceof List) {
            return (List<String>) rolesObj;
        }
        return Collections.emptyList();
    }

    /**
     * 获取权限列表
     */
    @SuppressWarnings("unchecked")
    public List<String> getPermissionsFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        Object permissionsObj = claims.get("permissions");
        if (permissionsObj instanceof List) {
            return (List<String>) permissionsObj;
        }
        return Collections.emptyList();
    }

    /**
     * 获取令牌剩余有效时间（秒）
     */
    public long getRemainingTime(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            long remaining = (expiration.getTime() - System.currentTimeMillis()) / 1000;
            return Math.max(0, remaining);
        } catch (Exception e) {
            log.debug("Failed to get token remaining time: {}", e.getMessage());
            return 0;
        }
    }

    /**
     * 检查令牌是否即将过期
     */
    public boolean isTokenExpiringSoon(String token, long thresholdSeconds) {
        long remainingTime = getRemainingTime(token);
        return remainingTime > 0 && remainingTime <= thresholdSeconds;
    }

    /**
     * 获取令牌签发时间
     */
    public Date getIssuedAt(String token) {
        return getClaimsFromToken(token).getIssuedAt();
    }

    /**
     * 获取令牌过期时间
     */
    public Date getExpiration(String token) {
        return getClaimsFromToken(token).getExpiration();
    }
}
