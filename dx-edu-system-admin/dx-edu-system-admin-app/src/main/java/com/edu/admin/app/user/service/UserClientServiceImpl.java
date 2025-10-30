package com.edu.admin.app.user.service;

import com.edu.admin.app.user.bean.cmd.cud.UserCreateCmd;
import com.edu.admin.app.user.bean.cmd.cud.UserDelCmd;
import com.edu.admin.app.user.bean.cmd.cud.UserUpdateCmd;
import com.edu.admin.app.user.bean.cmd.qry.UserPageQryCmd;
import com.edu.admin.app.user.bean.cmd.qry.UserQryCmd;
import com.edu.admin.app.user.executor.CreateUserExecutor;
import com.edu.admin.app.user.executor.DelUserExecutor;
import com.edu.admin.app.user.executor.QueryUserExecutor;
import com.edu.admin.app.user.executor.UpdateUserExecutor;
import com.edu.admin.client.user.api.UserClient;
import com.edu.admin.client.user.bean.dto.UserDto;
import com.edu.admin.client.user.bean.request.CreateUserRequest;
import com.edu.admin.client.user.bean.request.DelUserRequest;
import com.edu.admin.client.user.bean.request.QueryUserRequest;
import com.edu.admin.client.user.bean.request.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserClientServiceImpl implements UserClient {

    private final CreateUserExecutor createUserExecutor;

    private final DelUserExecutor delUserExecutor;

    private final UpdateUserExecutor updateUserExecutor;

    private final QueryUserExecutor queryUserExecutor;

    @GlobalTransactional
    @Override
    public UserDto save(CreateUserRequest request) {
        Assert.isTrue(!ObjectUtils.isEmpty(request.getName()), "name不能为空！");
        Assert.isTrue(!ObjectUtils.isEmpty(request.getPassword()), "password不能为空！");
        UserCreateCmd cmd = UserCreateCmd.builder().name(request.getName()).password(request.getPassword()).build();
        return createUserExecutor.execute(cmd);
    }

    @Override
    public UserDto update(UpdateUserRequest request) {
        Assert.isTrue(!ObjectUtils.isEmpty(request.getId()), "id不能为空！");
        UserUpdateCmd cmd =
            UserUpdateCmd.builder().id(request.getId()).name(request.getName()).password(request.getPassword()).build();
        return updateUserExecutor.execute(cmd);
    }

    @Override
    public UserDto delete(DelUserRequest request) {
        Assert.isTrue(!ObjectUtils.isEmpty(request.getId()), "id不能为空！");
        UserDelCmd cmd = UserDelCmd.builder().id(request.getId()).build();
        return delUserExecutor.execute(cmd);
    }

    @Override
    public UserDto details(QueryUserRequest request) {
        Assert.isTrue(!ObjectUtils.isEmpty(request.getId()), "id不能为空！");
        UserQryCmd cmd = UserQryCmd.builder().id(request.getId()).build();
        return queryUserExecutor.execute(cmd);
    }

    @Override
    public Page<UserDto> pagingQuery(QueryUserRequest request) {
        UserPageQryCmd cmd = UserPageQryCmd.builder()
            .name(request.getName())
            .pageNum(request.getPageNum())
            .pageSize(request.getPageSize())
            .id(request.getId())
            .build();
        return queryUserExecutor.execute(cmd);
    }
}
