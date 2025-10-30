package com.edu.admin.app.user.dubbo;


import com.edu.admin.app.user.bean.cmd.qry.UserQryCmd;
import com.edu.admin.app.user.executor.QueryUserExecutor;
import com.edu.admin.client.course.dubbo.UserClientDubbo;
import com.edu.admin.client.user.bean.dto.UserDto;
import com.edu.admin.client.user.bean.request.QueryUserRequest;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

@DubboService(version = "v1.0")
@RequiredArgsConstructor
public class UserClientDubboServiceImpl implements UserClientDubbo {

    private final QueryUserExecutor queryUserExecutor;

    @Override
    public UserDto detailsDubbo(QueryUserRequest request) {
        Assert.isTrue(!ObjectUtils.isEmpty(request.getId()), "id不能为空！");
        UserQryCmd cmd = UserQryCmd.builder()
            .id(request.getId())
            .build();
        return queryUserExecutor.execute(cmd);
    }
}
