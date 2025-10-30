package com.edu.admin.app.course.service;

import com.edu.admin.app.course.bean.cmd.cud.CourseCreateCmd;
import com.edu.admin.app.course.bean.cmd.cud.CourseDelCmd;
import com.edu.admin.app.course.bean.cmd.cud.CourseUpdateCmd;
import com.edu.admin.app.course.bean.cmd.qry.CoursePageQryCmd;
import com.edu.admin.app.course.bean.cmd.qry.CourseQryCmd;
import com.edu.admin.app.course.executor.CreateCourseExecutor;
import com.edu.admin.app.course.executor.DelCourseExecutor;
import com.edu.admin.app.course.executor.QueryCourseExecutor;
import com.edu.admin.app.course.executor.UpdateCourseExecutor;
import com.edu.admin.client.course.api.CourseClient;
import com.edu.admin.client.course.bean.dto.CourseDto;
import com.edu.admin.client.course.bean.request.CuCourseRequest;
import com.edu.admin.client.course.bean.request.QueryCourseRequest;
import com.edu.admin.client.course.bean.request.RdCourseRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * 课程client实现
 *
 * @author qianwj
 * @since 2025-10-22
 */
@Service
@RequiredArgsConstructor
public class CourseClientServiceImpl implements CourseClient {

    private final CreateCourseExecutor createCourseExecutor;

    private final UpdateCourseExecutor updateCourseExecutor;

    private final QueryCourseExecutor queryCourseExecutor;

    private final DelCourseExecutor delCourseExecutor;

    @Override
    public CourseDto save(CuCourseRequest save) {
        Assert.isTrue(null != save.getUserId(), "userId为空");
        Assert.isTrue(StringUtils.isNotEmpty(save.getCourseName()), "课程名称为空");
        CourseCreateCmd build =
            CourseCreateCmd.builder().userId(save.getUserId()).courseName(save.getCourseName()).build();
        return createCourseExecutor.execute(build);
    }

    @Override
    public CourseDto update(CuCourseRequest update) {
        Assert.isTrue(null != update.getId(), "课程id为空");
        Assert.isTrue(StringUtils.isNotEmpty(update.getCourseName()), "课程名称为空");
        CourseUpdateCmd build = CourseUpdateCmd.builder()
            .id(update.getId())
            .userId(update.getId())
            .courseName(update.getCourseName())
            .build();
        return updateCourseExecutor.execute(build);
    }

    @Override
    public void delete(RdCourseRequest delete) {
        Assert.isTrue(null != delete.getId(), "id为空");
        CourseDelCmd build = CourseDelCmd.builder().id(delete.getId()).build();
        delCourseExecutor.execute(build);
    }

    @Override
    public CourseDto details(RdCourseRequest details) {
        Assert.isTrue(null != details.getId(), "id为空");
        CourseQryCmd build = CourseQryCmd.builder().id(details.getId()).build();
        return queryCourseExecutor.execute(build);
    }

    @Override
    public Page<CourseDto> pagingQuery(QueryCourseRequest queryCourseRequest) {
        Assert.isTrue(queryCourseRequest.getPageNum() != null, "分页参数为空");
        Assert.isTrue(queryCourseRequest.getPageSize() != null, "分页参数为空");
        CoursePageQryCmd build = CoursePageQryCmd.builder()
            .pageNum(queryCourseRequest.getPageNum())
            .pageSize(queryCourseRequest.getPageSize())
            .courseName(queryCourseRequest.getCourseName())
            .build();
        return queryCourseExecutor.execute(build);
    }
}
