package com.edu.admin.app.teacher.service;

import com.edu.admin.app.teacher.bean.cmd.cud.TeacherAddCmd;
import com.edu.admin.app.teacher.executor.AddTeachersAgeExecutor;
import com.edu.admin.app.teacher.executor.CreateTeacherExecutor;
import com.edu.admin.client.teacher.api.TeacherClient;
import com.edu.admin.client.teacher.bean.dto.AddTeacherDto;
import com.edu.admin.client.teacher.bean.request.AddTeacherRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherClientServiceImpl implements TeacherClient {

    @Autowired
    private CreateTeacherExecutor createTeacherExecutor;

    @Autowired
    private AddTeachersAgeExecutor addTeachersAgeExecutor;

    @Override
    public AddTeacherDto saveTeacher(AddTeacherRequest addTeacherRequest) {
        TeacherAddCmd cmd = TeacherAddCmd.builder()
            .age(addTeacherRequest.getAge())
            .name(addTeacherRequest.getName())
            .phone(addTeacherRequest.getPhone())
            .address(addTeacherRequest.getAddress())
            .build();
        return createTeacherExecutor.execute(cmd);
    }

    @Override
    public void addTeachersAge() {
        addTeachersAgeExecutor.execute();
    }
}
