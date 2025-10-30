package com.edu.admin.app.user.convertor;

import com.edu.admin.client.user.bean.dto.UserDto;
import com.edu.admin.domain.bean.UserDo;

public class UserConvertor {
    public static UserDto convertDoToDto(UserDo resDo) {
        return UserDto.builder()
            .id(resDo.getId())
            .name(resDo.getName())
            .password(resDo.getPassword())
            .build();
    }
}
