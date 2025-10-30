package com.edu.admin.app.course.executor;

import com.edu.admin.app.course.bean.cmd.cud.CourseCreateCmd;
import com.edu.admin.app.course.convertor.CourseConvertor;
import com.edu.admin.client.course.bean.dto.CourseDto;
import com.edu.admin.domain.bean.CourseDo;
import com.edu.admin.domain.domainservice.CourseDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 课程创建
 *
 * @author qianwj
 * @since 2025-10-22
 */
@Service
@RequiredArgsConstructor
public class CreateCourseExecutor {

    private final CourseDomainService courseDomainService;

    public CourseDto execute(CourseCreateCmd cmd) {
        CourseDo save = courseDomainService.save(
            CourseDo.customBuilder().userId(cmd.getUserId()).courseName(cmd.getCourseName()).build());
        return CourseConvertor.convertDoToDto(save);
    }
}
