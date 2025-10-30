package com.edu.admin.infra.convertor;



import com.edu.admin.domain.bean.TeacherDO;
import com.edu.admin.infra.bean.entity.TeacherEntity;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 老师转换
 *
 * @author wangzhan
 * @since 2025-10-24
 */

public class TeacherInfraConvertor {

    public static TeacherDO entityToDo(TeacherEntity entity) {
        if (entity == null) {
            return new TeacherDO();
        }
        return TeacherDO.builder().id(entity.getId()).name(entity.getName())
                .age(entity.getAge()).phone(entity.getPhone()).address(entity.getAddress())
                .createdTime(entity.getCreatedTime()).updatedTime(entity.getUpdatedTime()).build();
    }

    public static TeacherEntity doToEntity(TeacherDO teacherDO) {
        if (teacherDO == null) {
            return new TeacherEntity();
        }
        return TeacherEntity.builder().id(teacherDO.getId()).name(teacherDO.getName())
                .age(teacherDO.getAge()).phone(teacherDO.getPhone()).address(teacherDO.getAddress())
                .createdTime(teacherDO.getCreatedTime()).updatedTime(teacherDO.getUpdatedTime()).build();
    }


    public static List<TeacherDO> entityToDoList(List<TeacherEntity> entities) {
        return entities.stream().map(TeacherInfraConvertor::entityToDo).collect(Collectors.toList());
    }
}
