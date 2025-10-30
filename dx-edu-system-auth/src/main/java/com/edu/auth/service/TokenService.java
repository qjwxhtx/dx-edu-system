package com.edu.auth.service;

import com.edu.auth.dto.AuthUser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 *
 * @author hjw
 * @since 2025-10-27
 */
@Slf4j
@Service
public class TokenService {

    @Value("${jwt.secret:mySuperSecretKeyForJWTGenerationInSpringCloudGateway2024}")
    private String jwtSecret;

    @Value("${jwt.expiration:3600000}")
    private long jwtExpiration;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        // 在PostConstruct中初始化，确保配置已注入
        if (jwtSecret == null || jwtSecret.trim().isEmpty()) {
            throw new IllegalStateException("JWT secret key must not be null or empty");
        }

        // 初始化密钥
        this.secretKey = new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");

        log.info("TokenService initialized with JWT expiration: {} ms", jwtExpiration);
    }

    /**
     * 生成JWT令牌
     */
    public String generateToken(AuthUser authUser) {
        return generateToken(authUser, Duration.ofMillis(jwtExpiration));
    }

    /**
     * 生成JWT令牌（自定义过期时间）
     */
    public String generateToken(AuthUser authUser, Duration duration) {
        try {
            Instant now = Instant.now();
            Instant expiryDate = now.plus(duration);

            // 构建JWT声明
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", authUser.getUserId());
            claims.put("tenantId", authUser.getTenantId());
            claims.put("username", authUser.getUsername());
            claims.put("email", authUser.getEmail());
            claims.put("roles", authUser.getRoles());
            claims.put("permissions", authUser.getPermissions());

            // 构建JWT
            return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .setClaims(claims)
                .setSubject(authUser.getUsername())
                .setIssuer("auth-service")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiryDate))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        } catch (Exception e) {
            throw new RuntimeException("JWT token generation failed", e);
        }
    }

    /**
     * 验证JWT令牌
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.debug("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 从令牌中提取用户信息
     */
    public AuthUser extractUserFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();

            Long userId = claims.get("userId", Long.class);
            Long tenantId = claims.get("tenantId", Long.class);
            String systemCode = claims.get("systemCode", String.class);
            String username = claims.getSubject();
            String email = claims.get("email", String.class);

            @SuppressWarnings("unchecked") List<String> roles = claims.get("roles", List.class);

            @SuppressWarnings("unchecked") List<String> permissions = claims.get("permissions", List.class);

            // 构建权限列表
            List<GrantedAuthority> authorities = new ArrayList<>();
            if (permissions != null) {
                authorities.addAll(permissions.stream().map(SimpleGrantedAuthority::new).toList());
            }
            if (roles != null) {
                authorities.addAll(roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList());
            }

            return new AuthUser(userId, tenantId, systemCode, username, null, // 不包含密码
                email, 1, authorities, roles != null ? roles : Collections.emptyList(),
                permissions != null ? permissions : Collections.emptyList());

        } catch (Exception e) {
            throw new RuntimeException("Invalid or expired token: " + e.getMessage(), e);
        }
    }

    /**
     * 获取令牌剩余时间
     */
    public Duration getRemainingTime(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();

            Date expiration = claims.getExpiration();
            return Duration.between(Instant.now(), expiration.toInstant());

        } catch (Exception e) {
            return Duration.ZERO;
        }
    }

    /**
     * 解析令牌但不验证（用于调试）
     */
    public Map<String, Object> parseTokenWithoutValidation(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                throw new RuntimeException("Invalid JWT token format");
            }

            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.readValue(payload, new TypeReference<Map<String, Object>>() {
            });

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JWT token", e);
        }
    }
}
