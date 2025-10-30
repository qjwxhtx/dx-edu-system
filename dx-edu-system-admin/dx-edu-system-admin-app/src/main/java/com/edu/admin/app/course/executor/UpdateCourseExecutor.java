package com.edu.admin.app.course.executor;

import com.edu.admin.app.course.bean.cmd.cud.CourseUpdateCmd;
import com.edu.admin.app.course.convertor.CourseConvertor;
import com.edu.admin.client.course.bean.dto.CourseDto;
import com.edu.admin.domain.bean.CourseDo;
import com.edu.admin.domain.domainservice.CourseDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author qianwj
 */
@Service
@RequiredArgsConstructor
public class UpdateCourseExecutor {

    private final CourseDomainService courseDomainService;

    public CourseDto execute(CourseUpdateCmd cmd) {
        CourseDo build =
            CourseDo.customBuilder().id(cmd.getId()).userId(cmd.getUserId()).courseName(cmd.getCourseName()).build();
        CourseDo courseDo = courseDomainService.update(build);
        return CourseConvertor.convertDoToDto(courseDo);
    }
}
