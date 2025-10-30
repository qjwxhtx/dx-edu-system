package com.edu.admin.app.teacher.bean.cmd.cud;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeacherAddCmd {
    private String name;
    private Integer age;
    private String phone;
    private String address;
}
