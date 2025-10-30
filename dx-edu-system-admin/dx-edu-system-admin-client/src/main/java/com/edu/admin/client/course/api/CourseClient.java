package com.edu.admin.client.course.api;


import com.edu.admin.client.course.bean.dto.CourseDto;
import com.edu.admin.client.course.bean.request.CuCourseRequest;
import com.edu.admin.client.course.bean.request.QueryCourseRequest;
import com.edu.admin.client.course.bean.request.RdCourseRequest;
import org.springframework.data.domain.Page;

/**
 * 课程client
 *
 * @author qianwj
 * @since 2025-10-22
 */

public interface CourseClient {

    CourseDto save(CuCourseRequest save);

    CourseDto update(CuCourseRequest update);

    void delete(RdCourseRequest delete);

    CourseDto details(RdCourseRequest details);

    Page<CourseDto> pagingQuery(QueryCourseRequest queryCourseRequest);
}
