package com.edu.admin.adapter.web.v1;

import com.edu.admin.client.ResResult;
import com.edu.admin.client.teacher.api.TeacherClient;
import com.edu.admin.client.teacher.bean.dto.AddTeacherDto;
import com.edu.admin.client.teacher.bean.request.AddTeacherRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 教师相关控制器
 */
@RestController
@RequestMapping("/web/v1/teacher")
public class TeacherController {

    @Autowired
    private TeacherClient teacherClient;

    /**
     * 新增教师信息
     *
     * @param addTeacherRequest 新增教师入参实体
     * @return ResResult<AddTeacherDto> 统一响应结果，包含保存成功的出参
     */
    @PostMapping(value = "/add")
    public ResResult<AddTeacherDto> saveTeacher(@RequestBody AddTeacherRequest addTeacherRequest) {
        AddTeacherDto response = teacherClient.saveTeacher(addTeacherRequest);
        return ResResult.okResult(response);
    }
}
