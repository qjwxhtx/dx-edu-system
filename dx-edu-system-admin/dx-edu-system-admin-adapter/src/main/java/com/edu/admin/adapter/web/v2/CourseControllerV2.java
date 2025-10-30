package com.edu.admin.adapter.web.v2;

import com.edu.admin.client.ResResult;
import com.edu.admin.client.course.api.CourseClient;
import com.edu.admin.client.course.bean.dto.CourseDto;
import com.edu.admin.client.course.bean.request.CuCourseRequest;
import com.edu.admin.client.course.bean.request.QueryCourseRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 课程Controller
 *
 * @author qianwj
 * @since 2025-10-22
 */
@RestController
@RequestMapping("/web/v2/course")
public class CourseControllerV2 {

    @Autowired
    private CourseClient courseClient;

    /**
     * 保存课程信息
     *
     * @param cuCourseRequest 课程创建/修改请求对象，包含课程的基本信息
     * @return ResResult<UserDto> 统一响应结果，包含保存成功的课程信息
     */
    @PostMapping(value = "/add")
    public ResResult<CourseDto> saveCourse(@RequestBody CuCourseRequest cuCourseRequest) {
        CourseDto response = courseClient.save(cuCourseRequest);
        return ResResult.okResult(response);
    }

    /**
     * 分页查询课程列表
     *
     * @param queryCourseRequest 包含分页查询条件的请求对象
     * @return 返回分页的课程信息列表
     */
    @GetMapping("/list")
    public ResResult<Page<CourseDto>> pagingQuery(@RequestBody QueryCourseRequest queryCourseRequest) {
        Page<CourseDto> response = courseClient.pagingQuery(queryCourseRequest);
        return ResResult.okResult(response);
    }
}
