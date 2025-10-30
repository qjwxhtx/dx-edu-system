package com.edu.admin.client.course.bean.request;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * Course新增/修改请求参数
 *
 * @author qianwj
 * @since 2025-10-22
 */
@Data
@Builder
public class CuCourseRequest implements Serializable {

    // 课程id
    private Long id;

    // 用户id
    private Long userId;

    // 课程名称
    private String courseName;
}
