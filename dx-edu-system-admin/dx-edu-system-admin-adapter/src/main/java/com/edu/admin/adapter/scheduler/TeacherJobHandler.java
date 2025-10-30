package com.edu.admin.adapter.scheduler;

import com.edu.admin.client.teacher.api.TeacherClient;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class TeacherJobHandler {

    @Resource
    private TeacherClient teacherClient;

    @XxlJob("addTeachersAgeHandler")
    public void addTeachersAgeHandler() {
        teacherClient.addTeachersAge();
    }
}
