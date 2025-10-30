package com.edu.admin.app.student.convertor;

import com.edu.admin.app.student.bean.cmd.cud.StudentCreateCmd;
import com.edu.admin.app.student.bean.cmd.cud.StudentUpdateCmd;
import com.edu.admin.client.student.bean.dto.StudentDto;
import com.edu.admin.client.student.bean.request.StudentSynRequest;
import com.edu.admin.domain.bean.StudentDo;

/**
 *
 *
 * @author hjw
 * @since 2025-10-22
 */
public class StudentConvertor {

    public static StudentDo createCmd2Do(StudentCreateCmd createCmd) {
        return StudentDo.builder()
            .studentCode(createCmd.getStudentCode())
            .memberCode(createCmd.getMemberCode())
            .loginName(createCmd.getLoginName())
            .loginPwd(createCmd.getLoginPwd())
            .realName(createCmd.getRealName())
            .grade(createCmd.getGrade())
            .school(createCmd.getSchool())
            .address(createCmd.getAddress())
            .regTime(createCmd.getRegTime())
            .isEnable(createCmd.getIsEnable())
            .restrictedUse(createCmd.getRestrictedUse())
            .gender(createCmd.getGender())
            .useModule(createCmd.getUseModule())
            .isFormal(createCmd.getIsFormal())
            .dbStatus(createCmd.getDbStatus())
            .deliveryId(createCmd.getDeliveryId())
            .learnTubeId(createCmd.getLearnTubeId())
            .province(createCmd.getProvince())
            .city(createCmd.getCity())
            .area(createCmd.getArea())
            .archive(createCmd.getArchive())
            .voiceModel(createCmd.getVoiceModel())
            .build();
    }

    public static StudentDo updateCmd2Do(StudentUpdateCmd updateCmd) {
        return StudentDo.builder()
            .studentCode(updateCmd.getStudentCode())
            .memberCode(updateCmd.getMemberCode())
            .loginName(updateCmd.getLoginName())
            .loginPwd(updateCmd.getLoginPwd())
            .realName(updateCmd.getRealName())
            .grade(updateCmd.getGrade())
            .school(updateCmd.getSchool())
            .address(updateCmd.getAddress())
            .regTime(updateCmd.getRegTime())
            .isEnable(updateCmd.getIsEnable())
            .restrictedUse(updateCmd.getRestrictedUse())
            .gender(updateCmd.getGender())
            .useModule(updateCmd.getUseModule())
            .isFormal(updateCmd.getIsFormal())
            .dbStatus(updateCmd.getDbStatus())
            .deliveryId(updateCmd.getDeliveryId())
            .learnTubeId(updateCmd.getLearnTubeId())
            .province(updateCmd.getProvince())
            .city(updateCmd.getCity())
            .area(updateCmd.getArea())
            .archive(updateCmd.getArchive())
            .voiceModel(updateCmd.getVoiceModel())
            .build();
    }

    public static StudentDto do2Dto(StudentDo studentDo) {
        return StudentDto.builder()
            .studentCode(studentDo.getStudentCode())
            .memberCode(studentDo.getMemberCode())
            .loginName(studentDo.getLoginName())
            .loginPwd(studentDo.getLoginPwd())
            .realName(studentDo.getRealName())
            .grade(studentDo.getGrade())
            .school(studentDo.getSchool())
            .address(studentDo.getAddress())
            .regTime(studentDo.getRegTime())
            .isEnable(studentDo.getIsEnable())
            .restrictedUse(studentDo.getRestrictedUse())
            .gender(studentDo.getGender())
            .useModule(studentDo.getUseModule())
            .isFormal(studentDo.getIsFormal())
            .dbStatus(studentDo.getDbStatus())
            .deliveryId(studentDo.getDeliveryId())
            .learnTubeId(studentDo.getLearnTubeId())
            .province(studentDo.getProvince())
            .city(studentDo.getCity())
            .area(studentDo.getArea())
            .archive(studentDo.getArchive())
            .voiceModel(studentDo.getVoiceModel())
            .build();
    }

    public static StudentCreateCmd request2CreateCmd(StudentSynRequest synRequest) {
        return StudentCreateCmd.builder()
            .studentCode(synRequest.getStudentCode())
            .memberCode(synRequest.getMemberCode())
            .loginName(synRequest.getLoginName())
            .build();
    }

    public static StudentUpdateCmd request2UpdateCmd(StudentSynRequest synRequest) {
        return StudentUpdateCmd.builder()
            .studentCode(synRequest.getStudentCode())
            .memberCode(synRequest.getMemberCode())
            .loginName(synRequest.getLoginName())
            .build();
    }

}
