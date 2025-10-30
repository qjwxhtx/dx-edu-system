package com.edu.admin.client.user.bean.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDto {

    private Long id;
    private String name;
    private String password;


}
