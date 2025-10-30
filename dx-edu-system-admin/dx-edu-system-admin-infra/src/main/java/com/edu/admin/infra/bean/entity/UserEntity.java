package com.edu.admin.infra.bean.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@Data
@Entity
@DynamicUpdate
@Table(name = "user")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = cn.hutool.core.util.IdUtil.getSnowflake().nextId();
        }
    }

}
