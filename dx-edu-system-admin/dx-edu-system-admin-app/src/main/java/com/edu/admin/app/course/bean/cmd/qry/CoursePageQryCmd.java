package com.edu.admin.app.course.bean.cmd.qry;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页查询
 *
 * @author qianwj
 * @since 2025-10-22
 */
@Data
@Builder
public class CoursePageQryCmd implements Serializable {

    // 课程id
    private Long id;

    // 用户id
    private Long userId;

    // 课程名称
    private String courseName;

    private Integer pageNum;
    private Integer pageSize;
}
