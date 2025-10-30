package com.edu.admin.client.course.bean.request;

import lombok.Data;

/**
 * 分页查询课程
 *
 * @author qianwj
 * @since 2025-10-22
 */
@Data
public class QueryCourseRequest {

    // 课程id
    private Long id;

    // 用户id
    private Long userId;

    // 课程名称
    private String courseName;

    private Integer pageNum;
    private Integer pageSize;
}
