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
 * 菜单实体
 *
 * @author hjw
 * @since 2025-10-27
 */
@Data
@Entity
@Table(name = "s_menu")
@EntityListeners(AuditingEntityListener.class)
public class SMenuEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "system_id", nullable = false)
    private Long systemId;

    @Column(name = "parent_id")
    private Long parentId = 0L;

    @Column(name = "menu_code", nullable = false)
    private String menuCode;

    @Column(name = "menu_name", nullable = false)
    private String menuName;

    @Column(name = "menu_type")
    private Integer menuType = 1;

    private String path;

    private String component;

    private String icon;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    private String permissions;

    private Integer status = 1;

    @CreatedDate
    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @LastModifiedDate
    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

}
