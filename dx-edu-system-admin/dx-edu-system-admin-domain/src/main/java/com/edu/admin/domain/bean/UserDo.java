package com.edu.admin.domain.bean;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDo {
    private Long id;
    private String name;
    private String password;

    private Integer pageNum;
    private Integer pageSize;
}
