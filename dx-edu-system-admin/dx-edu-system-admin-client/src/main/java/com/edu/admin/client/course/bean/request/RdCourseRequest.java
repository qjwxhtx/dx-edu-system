package com.edu.admin.client.course.bean.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 获取/删除请求参数
 *
 * @author qianwj
 * @since 2025-10-22
 */
@Data
public class RdCourseRequest implements Serializable {

    // 课程id
    private Long id;
}
