package com.edu.admin.app.user.executor;

import com.edu.admin.app.user.bean.cmd.qry.UserPageQryCmd;
import com.edu.admin.app.user.bean.cmd.qry.UserQryCmd;
import com.edu.admin.app.user.convertor.UserConvertor;
import com.edu.admin.client.user.bean.dto.UserDto;
import com.edu.admin.domain.bean.UserDo;
import com.edu.admin.domain.domainservice.UserDomainService;
import com.edu.admin.domain.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueryUserExecutor {

    private final UserRepo userRepo;
    private final UserDomainService userDomainService;

    public UserDto execute(UserQryCmd cmd) {
        UserDo userDo =
            UserDo.builder().id(cmd.getId()).build();
        UserDo resDo = userDomainService.detail(userDo);
        return UserConvertor.convertDoToDto(resDo);
    }

    public Page<UserDto> execute(UserPageQryCmd cmd) {
        UserDo userDo = UserDo.builder().id(cmd.getId()).name(cmd.getName()).pageNum(cmd.getPageNum())
            .pageSize(cmd.getPageSize()).build();
        Page<UserDo> userDtoPage = userRepo.pageList(userDo);
        return userDtoPage.map(UserConvertor::convertDoToDto);
    }

}
