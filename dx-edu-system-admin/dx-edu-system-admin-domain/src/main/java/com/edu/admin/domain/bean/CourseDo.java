package com.edu.admin.domain.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 课程DO
 *
 * @author qianwj
 * @since 2025-10-22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "customBuilder") // 改名！避免冲突
public class CourseDo extends PageDo<CourseDo> implements Serializable {

    // 课程id
    private Long id;

    // 用户id
    private Long userId;

    // 课程名称
    private String courseName;
}
