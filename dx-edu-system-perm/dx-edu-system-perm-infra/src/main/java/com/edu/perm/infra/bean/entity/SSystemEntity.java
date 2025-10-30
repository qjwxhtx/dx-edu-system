package com.edu.perm.infra.bean.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 系统信息实体
 *
 * @author hjw
 * @since 2025-10-29
 */
@Data
@Entity
@Table(name = "s_system")
@EntityListeners(AuditingEntityListener.class)
public class SSystemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "system_code", nullable = false)
    private String systemCode;

    @Column(name = "system_name", nullable = false)
    private String systemName;

    private String description;

    private Integer status = 1;

    @CreatedDate
    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @LastModifiedDate
    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

}
