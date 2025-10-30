package com.edu.admin.app.course.bean.cmd.cud;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 删除
 *
 * @author qianwj
 * @since 2025-10-22
 */
@Data
@Builder
public class CourseDelCmd implements Serializable {

    private Long id;
}
