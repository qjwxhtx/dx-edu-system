package com.edu.admin.app.course.executor;

import com.edu.admin.app.course.bean.cmd.cud.CourseDelCmd;
import com.edu.admin.domain.bean.CourseDo;
import com.edu.admin.domain.domainservice.CourseDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author qianwj
 */
@Service
@RequiredArgsConstructor
public class DelCourseExecutor {

    private final CourseDomainService courseDomainService;

    public void execute(CourseDelCmd cmd) {
        courseDomainService.delete(CourseDo.customBuilder().id(cmd.getId()).build());
    }
}
