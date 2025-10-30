package com.edu.admin.app.course.convertor;

import com.edu.admin.client.course.bean.dto.CourseDto;
import com.edu.admin.domain.bean.CourseDo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * DO转换DTO
 *
 * @author qianwj
 * @since 2025-10-22
 */

public class CourseConvertor {

    public static CourseDto convertDoToDto(CourseDo courseDo) {
        return CourseDto.builder()
            .id(courseDo.getId())
            .courseName(courseDo.getCourseName())
            .userId(courseDo.getUserId())
            .build();
    }

    public static List<CourseDto> convertDoToDto(List<CourseDo> courseDoList) {
        return courseDoList.stream().map(CourseConvertor::convertDoToDto).collect(Collectors.toList());
    }
}
