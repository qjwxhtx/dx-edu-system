package com.edu.admin.app.course.bean.cmd.cud;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 更新
 *
 * @author qianwj
 * @since 2025-10-22
 */
@Data
@Builder
public class CourseUpdateCmd implements Serializable {

    // 课程id
    private Long id;

    // 用户id
    private Long userId;

    // 课程名称
    private String courseName;
}
