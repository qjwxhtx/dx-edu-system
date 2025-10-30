package com.edu.admin.app.teacher.executor;

import com.edu.admin.domain.domainservice.TeacherDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddTeachersAgeExecutor {
    @Autowired
    private TeacherDomainService teacherDomainService;

    public void execute() {
        teacherDomainService.addTeachersAge();
    }
}
