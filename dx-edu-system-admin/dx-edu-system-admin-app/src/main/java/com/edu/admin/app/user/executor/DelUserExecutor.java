package com.edu.admin.app.user.executor;

import com.edu.admin.app.user.bean.cmd.cud.UserDelCmd;
import com.edu.admin.app.user.convertor.UserConvertor;
import com.edu.admin.client.user.bean.dto.UserDto;
import com.edu.admin.domain.bean.UserDo;
import com.edu.admin.domain.domainservice.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DelUserExecutor {

    private final UserDomainService userDomainService;

    public UserDto execute(UserDelCmd cmd) {
        UserDo userDo =
            UserDo.builder().id(cmd.getId()).build();
        UserDo resDo = userDomainService.delete(userDo);
        return UserConvertor.convertDoToDto(resDo);
    }
}
