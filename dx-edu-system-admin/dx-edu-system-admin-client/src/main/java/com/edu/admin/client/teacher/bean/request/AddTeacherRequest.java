package com.edu.admin.client.teacher.bean.request;

import lombok.Data;

@Data
public class AddTeacherRequest {
    private String name;
    private Integer age;
    private String phone;
    private String address;
}
