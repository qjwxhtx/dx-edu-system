package com.edu.admin.app.student.bean.cmd.cud;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 *
 *
 * @author hjw
 * @since 2025-10-22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentCreateCmd {

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
