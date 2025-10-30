package com.edu.admin.app.course.executor;

import com.edu.admin.app.course.bean.cmd.qry.CoursePageQryCmd;
import com.edu.admin.app.course.bean.cmd.qry.CourseQryCmd;
import com.edu.admin.app.course.convertor.CourseConvertor;
import com.edu.admin.client.course.bean.dto.CourseDto;
import com.edu.admin.domain.bean.CourseDo;
import com.edu.admin.domain.domainservice.CourseDomainService;
import com.edu.admin.domain.repo.CourseRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * @author qianwj
 */
@Service
@RequiredArgsConstructor
public class QueryCourseExecutor {

    private final CourseRepo courseRepo;

    private final CourseDomainService courseDomainService;

    public CourseDto execute(CourseQryCmd cmd) {
        CourseDo courseDo = CourseDo.customBuilder().id(cmd.getId()).build();
        CourseDo resDo = courseDomainService.detail(courseDo);
        return CourseConvertor.convertDoToDto(resDo);
    }

    public Page<CourseDto> execute(CoursePageQryCmd cmd) {
        CourseDo build = CourseDo.customBuilder().build();
        build.setPageSize(cmd.getPageSize());
        build.setPageNum(cmd.getPageNum());
        build.setCourseName(cmd.getCourseName());
        Page<CourseDo> doPage = courseRepo.pageList(build);
        return doPage.map(CourseConvertor::convertDoToDto);
    }

}
