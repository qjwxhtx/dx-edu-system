package com.edu.admin.infra.convertor;

import com.edu.admin.domain.bean.StudentDo;
import com.edu.admin.infra.bean.entity.StudentEntity;

/**
 *
 *
 * @author hjw
 * @since 2025-10-23
 */
public class StudentInfraConvertor {

    public static StudentDo entity2Do(StudentEntity studentEntity) {
        if (studentEntity == null) {
            return new StudentDo();
        }

        return StudentDo.builder()
            .studentCode(studentEntity.getStudentCode())
            .memberCode(studentEntity.getMemberCode())
            .build();
    }

    public static StudentEntity do2Entity(StudentDo studentDo) {
        if (studentDo == null) {
            return new StudentEntity();
        }

        return StudentEntity.builder()
            .studentCode(studentDo.getStudentCode())
            .memberCode(studentDo.getMemberCode())
            .build();
    }

}
