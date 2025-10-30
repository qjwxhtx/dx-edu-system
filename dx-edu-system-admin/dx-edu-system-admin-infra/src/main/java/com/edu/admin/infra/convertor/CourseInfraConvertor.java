package com.edu.admin.infra.convertor;



import com.edu.admin.domain.bean.CourseDo;
import com.edu.admin.infra.bean.entity.CourseEntity;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 课程转换
 *
 * @author qianwj
 * @since 2025-10-22
 */

public class CourseInfraConvertor {

    public static CourseDo entityToDo(CourseEntity entity) {
        return CourseDo.customBuilder()
            .id(entity.getId())
            .userId(entity.getUserId())
            .courseName(entity.getCourseName())
            .build();
    }

    public static CourseEntity doToEntity(CourseDo dto) {
        return CourseEntity.builder().id(dto.getId()).userId(dto.getUserId()).courseName(dto.getCourseName()).build();
    }


    public static List<CourseDo> entityToDoList(List<CourseEntity> entities) {
        return entities.stream().map(CourseInfraConvertor::entityToDo).collect(Collectors.toList());
    }
}
