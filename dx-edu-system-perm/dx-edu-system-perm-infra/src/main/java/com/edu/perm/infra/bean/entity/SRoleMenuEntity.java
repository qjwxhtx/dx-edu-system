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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 角色菜单中间表
 *
 * @author hjw
 * @since 2025-10-27
 */
@Data
@Entity
@Table(name = "s_role_menu")
@EntityListeners(AuditingEntityListener.class)
public class SRoleMenuEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "menu_id")
    private Long menuId;

    @CreatedDate
    @Column(name = "created_time")
    private LocalDateTime createdTime;

}
