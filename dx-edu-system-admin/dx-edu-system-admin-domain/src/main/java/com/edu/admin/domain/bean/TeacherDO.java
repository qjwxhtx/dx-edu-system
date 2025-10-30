package com.edu.admin.domain.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDO {
    private Long id;

    private String name;

    private Integer age;

    private String phone;

    private String address;

    private Date createdTime;

    private Date updatedTime;
}
