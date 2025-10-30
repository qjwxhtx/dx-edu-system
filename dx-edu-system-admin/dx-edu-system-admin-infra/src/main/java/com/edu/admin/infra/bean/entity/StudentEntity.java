package com.edu.admin.infra.bean.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 学员Entity
 *
 * @author hjw
 * @since 2025-10-22
 */
@Data
@Entity
@Builder
@Table(name = "student")
@NoArgsConstructor
@AllArgsConstructor
public class StudentEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_code")
    private String studentCode;

    @Column(name = "member_code")
    private String memberCode;

    @Column(name = "login_name")
    private String loginName;

    @Column(name = "login_pwd")
    private String loginPwd;

    @Column(name = "real_name")
    private String realName;

    @Column(name = "grade")
    private Integer grade;

    @Column(name = "school")
    private String school;

    @Column(name = "address")
    private String address;

    @Column(name = "reg_time")
    private Date regTime;

    @Column(name = "is_enable")
    private Integer isEnable;

    @Column(name = "restricted_use")
    private Integer restrictedUse;

    @Column(name = "gender")
    private String gender;

    @Column(name = "use_module")
    private Integer useModule;

    @Column(name = "is_formal")
    private Integer isFormal;

    @Column(name = "db_status")
    private String dbStatus;

    @Column(name = "delivery_id")
    private String deliveryId;

    @Column(name = "learn_tube_id")
    private String learnTubeId;

    @Column(name = "province")
    private String province;

    @Column(name = "city")
    private String city;

    @Column(name = "area")
    private String area;

    @Column(name = "archive")
    private Date archive;

    @Column(name = "voice_model")
    private String voiceModel;

}
