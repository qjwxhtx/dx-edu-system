package com.edu.admin.client.teacher.api;

import com.edu.admin.client.teacher.bean.dto.AddTeacherDto;
import com.edu.admin.client.teacher.bean.request.AddTeacherRequest;

public interface TeacherClient {
    /**
     * 保存教师信息
     *
     * @param addTeacherRequest
     * @return
     */
    AddTeacherDto saveTeacher(AddTeacherRequest addTeacherRequest);

    /**
     * 为每位教师的年龄加1
     */
    void addTeachersAge();
}
