package com.edu.admin.app.course.bean.cmd.qry;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 查询
 *
 * @author qianwj
 * @since 2025-10-22
 */
@Data
@Builder
public class CourseQryCmd implements Serializable {

    // 课程id
    private Long id;

    // 用户id
    private Long userId;

    // 课程名称
    private String courseName;
}
