package com.edu.admin.infra.convertor;

import com.edu.admin.domain.bean.UserDo;
import com.edu.admin.infra.bean.entity.UserEntity;

public class UserInfraConvertor {
    public static UserEntity convertDoToEntity(UserDo userDo) {
        if (userDo == null) {
            return new UserEntity();
        }
        UserEntity user = new UserEntity();
        user.setId(userDo.getId());
        user.setName(userDo.getName());
        user.setPassword(userDo.getPassword());
        return user;
    }

    public static UserDo convertEntityToDo(UserEntity userDo) {
        return UserDo.builder()
            .id(userDo.getId())
            .name(userDo.getName())
            .password(userDo.getPassword())
            .build();
    }
}
