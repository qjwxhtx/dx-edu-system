package com.edu.admin.client.user.bean.request;

import lombok.Data;

@Data
public class CreateUserRequest {

    private Long id;
    private String name;
    private String password;

}
