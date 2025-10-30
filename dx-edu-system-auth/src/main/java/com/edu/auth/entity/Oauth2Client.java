package com.edu.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 *
 *
 * @author hjw
 * @since 2025-10-28
 */
@Data
@Entity
@Table(name = "oauth2_client")
public class Oauth2Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "client_id", nullable = false, unique = true)
    private String clientId;

    @Column(name = "client_secret", nullable = false)
    private String clientSecret;

    @Column(name = "client_authentication_methods", length = 1000)
    private String clientAuthenticationMethods;

    @Column(name = "authorization_grant_types", length = 1000)
    private String authorizationGrantTypes;

    @Column(name = "scopes", length = 1000)
    private String scopes;

    @Column(name = "redirect_uris", length = 1000)
    private String redirectUris;

    @Column(name = "client_settings", length = 2000)
    private String clientSettings;

    @Column(name = "token_settings", length = 2000)
    private String tokenSettings;

    @CreationTimestamp
    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @UpdateTimestamp
    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

}
