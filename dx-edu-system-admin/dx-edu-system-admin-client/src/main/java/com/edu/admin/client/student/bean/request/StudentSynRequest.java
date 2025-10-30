package com.edu.admin.client.student.bean.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 学员同步请求实体
 *
 * @author hjw
 * @since 2025-10-22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentSynRequest {

    private String studentCode;

    private String memberCode;

    private String loginName;

    private String loginPwd;

    private String realName;

    private Integer grade;

    private String school;

    private String address;

    private Date regTime;

    private Integer isEnable;

    private Integer restrictedUse;

    private String gender;

    private Integer useModule;

    private Integer isFormal;

    private String dbStatus;

    private String deliveryId;

    private String learnTubeId;

    private String province;

    private String city;

    private String area;

    private Date archive;

    private String voiceModel;

}
