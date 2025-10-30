package com.edu.auth.controller;

import com.edu.auth.dto.ApiResponse;
import com.edu.auth.dto.AuthUser;
import com.edu.auth.dto.LoginRequest;
import com.edu.auth.dto.LoginResponse;
import com.edu.auth.dto.UserInfo;
import com.edu.auth.dto.ValidateTokenResponse;
import com.edu.auth.repository.SUserRepository;
import com.edu.auth.service.TokenService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 *
 * @author hjw
 * @since 2025-10-27
 */
@RestController
@RequestMapping("/api")
@Validated
public class AuthController {

    private final TokenService tokenService;

    private final AuthenticationManager authenticationManager;

    private final SUserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(TokenService tokenService, AuthenticationManager authenticationManager,
        SUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        try {
            // 认证用户
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                buildUsernameWithTenant(request.getUsername(), request.getTenantCode()), request.getPassword()));

            AuthUser authUser = (AuthUser) authentication.getPrincipal();

            // 生成令牌
            String token = tokenService.generateToken(authUser);

            // 构建响应
            LoginResponse response = LoginResponse.builder()
                .accessToken("Bearer " + token)
                .tokenType("Bearer")
                .expiresIn(3600L)
                .userInfo(buildUserInfo(authUser))
                .build();

            return ResponseEntity.ok(ApiResponse.success(response));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("认证失败: " + e.getMessage()));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = extractTokenFromHeader(authHeader);
            AuthUser authUser = tokenService.extractUserFromToken(token);

            // 生成新令牌
            String newToken = tokenService.generateToken(authUser);

            LoginResponse response = LoginResponse.builder()
                .accessToken(newToken)
                .tokenType("Bearer")
                .expiresIn(3600L)
                .userInfo(buildUserInfo(authUser))
                .build();

            return ResponseEntity.ok(ApiResponse.success(response));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("令牌刷新失败: " + e.getMessage()));
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<ApiResponse<ValidateTokenResponse>> validateToken(
        @RequestHeader("Authorization") String authHeader) {
        try {
            String token = extractTokenFromHeader(authHeader);
            boolean isValid = tokenService.validateToken(token);

            ValidateTokenResponse response = ValidateTokenResponse.builder()
                .valid(isValid)
                .remainingTime(isValid ? tokenService.getRemainingTime(token).getSeconds() : 0)
                .build();

            return ResponseEntity.ok(ApiResponse.success(response));

        } catch (Exception e) {
            return ResponseEntity.ok(
                ApiResponse.success(ValidateTokenResponse.builder().valid(false).remainingTime(0L).build()));
        }
    }

    private String buildUsernameWithTenant(String username, String tenantCode) {
        return username + "@" + tenantCode;
    }

    private UserInfo buildUserInfo(AuthUser authUser) {
        return UserInfo.builder()
            .userId(authUser.getUserId())
            .username(authUser.getUsername())
            .email(authUser.getEmail())
            .tenantId(authUser.getTenantId())
            .roles(authUser.getRoles())
            .permissions(authUser.getPermissions())
            .build();
    }

    private String extractTokenFromHeader(String authHeader) {
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new RuntimeException("Invalid authorization header");
    }
}
