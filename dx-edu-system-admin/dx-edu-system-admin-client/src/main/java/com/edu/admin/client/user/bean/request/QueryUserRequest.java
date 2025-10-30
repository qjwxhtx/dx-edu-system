package com.edu.admin.client.user.bean.request;

import lombok.Data;

@Data
public class QueryUserRequest {

    private Long id;
    private String name;
    private String password;

    private Integer pageNum;
    private Integer pageSize;
}
