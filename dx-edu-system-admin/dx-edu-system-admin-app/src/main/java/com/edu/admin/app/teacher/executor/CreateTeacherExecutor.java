package com.edu.admin.app.teacher.executor;


import com.edu.admin.app.teacher.bean.cmd.cud.TeacherAddCmd;
import com.edu.admin.client.teacher.bean.dto.AddTeacherDto;
import com.edu.admin.domain.bean.TeacherDO;
import com.edu.admin.domain.domainservice.TeacherDomainService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class CreateTeacherExecutor {
    @Autowired
    private TeacherDomainService teacherDomainService;

    public AddTeacherDto execute(TeacherAddCmd cmd) {
        Assert.isTrue(StringUtils.isNotBlank(cmd.getName()), "教师姓名为空");

        TeacherDO teacherDO = TeacherDO.builder().age(cmd.getAge()).name(cmd.getName()).phone(cmd.getPhone())
                .address(cmd.getAddress()).build();
        TeacherDO save = teacherDomainService.save(teacherDO);
        return AddTeacherDto.builder().id(save.getId()).build();
    }
}
