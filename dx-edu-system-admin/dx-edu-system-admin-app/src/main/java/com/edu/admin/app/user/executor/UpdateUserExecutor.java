package com.edu.admin.app.user.executor;

import com.edu.admin.app.user.bean.cmd.cud.UserUpdateCmd;
import com.edu.admin.app.user.convertor.UserConvertor;
import com.edu.admin.client.user.bean.dto.UserDto;
import com.edu.admin.domain.bean.UserDo;
import com.edu.admin.domain.domainservice.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateUserExecutor {

    private final UserDomainService userDomainService;

    public UserDto execute(UserUpdateCmd cmd) {
        UserDo userDo =
            UserDo.builder().id(cmd.getId()).name(cmd.getName()).password(cmd.getPassword()).build();
        UserDo resDo = userDomainService.update(userDo);
        return UserConvertor.convertDoToDto(resDo);
    }
}
