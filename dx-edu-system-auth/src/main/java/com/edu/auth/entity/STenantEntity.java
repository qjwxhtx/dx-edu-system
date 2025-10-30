package com.edu.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 租户实体
 *
 * @author hjw
 * @since 2025-10-29
 */
@Data
@Entity
@Table(name = "s_tenant")
@EntityListeners(AuditingEntityListener.class)
public class STenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "system_id", nullable = false)
    private Long systemId;

    @Column(name = "tenant_code")
    private String tenantCode;

    @Column(name = "tenant_name")
    private String tenantName;

    private Integer status;

}
