package com.edu.admin.client.course.bean.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 课程dto : todo 最终接口返回的DTO ，如dto中包含实体字段，该实体类在app对应bean包下dto包中进行定义
 *
 * @author qianwj
 * @since 2025-10-22
 */
@Data
@Builder
public class CourseDto implements Serializable {

    // 课程id
    private Long id;

    // 用户id
    private Long userId;

    // 课程名称
    private String courseName;
}
