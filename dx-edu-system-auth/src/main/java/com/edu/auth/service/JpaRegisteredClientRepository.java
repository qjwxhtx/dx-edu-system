package com.edu.auth.service;

import com.edu.auth.entity.Oauth2Client;
import com.edu.auth.repository.Oauth2ClientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 动态注册客户端仓库
 *
 * @author hjw
 * @since 2025-10-28
 */
@Component
@RequiredArgsConstructor
public class JpaRegisteredClientRepository implements RegisteredClientRepository {

    private final Oauth2ClientRepository oauth2ClientRepository;

    @Override
    public void save(RegisteredClient registeredClient) {
        // 保存逻辑 - 根据需求实现
        Oauth2Client client = convertToOAuth2Client(registeredClient);
        oauth2ClientRepository.save(client);
    }

    @Override
    public RegisteredClient findById(String id) {
        return oauth2ClientRepository.findById(id).map(this::convertToRegisteredClient).orElse(null);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        return oauth2ClientRepository.findByClientId(clientId).map(this::convertToRegisteredClient).orElse(null);
    }

    private RegisteredClient convertToRegisteredClient(Oauth2Client client) {
        try {
            return RegisteredClient.withId(client.getId())
                .clientId(client.getClientId())
                .clientSecret(client.getClientSecret())
                .clientAuthenticationMethods(clientAuthenticationMethods -> clientAuthenticationMethods.addAll(
                    parseClientAuthenticationMethods(client.getClientAuthenticationMethods())))
                .authorizationGrantTypes(authorizationGrantTypes -> authorizationGrantTypes.addAll(
                    parseAuthorizationGrantTypes(client.getAuthorizationGrantTypes())))
                .scopes(scopes -> scopes.addAll(parseScopes(client.getScopes())))
                .redirectUris(redirectUris -> redirectUris.addAll(parseRedirectUris(client.getRedirectUris())))
                .clientSettings(parseClientSettings(client.getClientSettings()))
                .tokenSettings(parseTokenSettings(client.getTokenSettings()))
                .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert OAuth2Client to RegisteredClient: " + client.getClientId(),
                e);
        }
    }

    private Oauth2Client convertToOAuth2Client(RegisteredClient registeredClient) {
        Oauth2Client client = new Oauth2Client();
        client.setId(registeredClient.getId());
        client.setClientId(registeredClient.getClientId());
        client.setClientSecret(registeredClient.getClientSecret());
        client.setClientAuthenticationMethods(registeredClient.getClientAuthenticationMethods()
            .stream()
            .map(ClientAuthenticationMethod::getValue)
            .collect(Collectors.joining(",")));
        client.setAuthorizationGrantTypes(registeredClient.getAuthorizationGrantTypes()
            .stream()
            .map(AuthorizationGrantType::getValue)
            .collect(Collectors.joining(",")));
        client.setScopes(String.join(",", registeredClient.getScopes()));
        client.setRedirectUris(String.join(",", registeredClient.getRedirectUris()));

        // 序列化 settings
        client.setClientSettings(serializeClientSettings(registeredClient.getClientSettings()));
        client.setTokenSettings(serializeTokenSettings(registeredClient.getTokenSettings()));

        return client;
    }

    // 解析方法
    private Set<ClientAuthenticationMethod> parseClientAuthenticationMethods(String methods) {
        if (methods == null || methods.trim().isEmpty()) {
            return Set.of(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
        }
        return Arrays.stream(methods.split(","))
            .map(String::trim)
            .map(ClientAuthenticationMethod::new)
            .collect(Collectors.toSet());
    }

    private Set<AuthorizationGrantType> parseAuthorizationGrantTypes(String grantTypes) {
        if (grantTypes == null || grantTypes.trim().isEmpty()) {
            return Set.of(AuthorizationGrantType.AUTHORIZATION_CODE);
        }
        return Arrays.stream(grantTypes.split(","))
            .map(String::trim)
            .map(AuthorizationGrantType::new)
            .collect(Collectors.toSet());
    }

    private Set<String> parseScopes(String scopes) {
        if (scopes == null || scopes.trim().isEmpty()) {
            return Set.of("openid", "profile");
        }
        return Arrays.stream(scopes.split(",")).map(String::trim).collect(Collectors.toSet());
    }

    private Set<String> parseRedirectUris(String redirectUris) {
        if (redirectUris == null || redirectUris.trim().isEmpty()) {
            return Set.of();
        }
        return Arrays.stream(redirectUris.split(",")).map(String::trim).collect(Collectors.toSet());
    }

    private ClientSettings parseClientSettings(String clientSettingsJson) {
        if (clientSettingsJson == null || clientSettingsJson.trim().isEmpty()) {
            return ClientSettings.builder().build();
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> settingsMap = mapper.readValue(clientSettingsJson, Map.class);
            return ClientSettings.withSettings(settingsMap).build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse client settings", e);
        }
    }

    private TokenSettings parseTokenSettings(String tokenSettingsJson) {
        if (tokenSettingsJson == null || tokenSettingsJson.trim().isEmpty()) {
            return TokenSettings.builder()
                .accessTokenTimeToLive(Duration.ofHours(1))
                .refreshTokenTimeToLive(Duration.ofDays(30))
                .build();
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> settingsMap = mapper.readValue(tokenSettingsJson, Map.class);
            return TokenSettings.withSettings(settingsMap).build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse token settings", e);
        }
    }

    private String serializeClientSettings(ClientSettings clientSettings) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(clientSettings.getSettings());
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize client settings", e);
        }
    }

    private String serializeTokenSettings(TokenSettings tokenSettings) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(tokenSettings.getSettings());
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize token settings", e);
        }
    }
}
