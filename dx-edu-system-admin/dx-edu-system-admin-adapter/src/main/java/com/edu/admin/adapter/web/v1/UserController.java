package com.edu.admin.adapter.web.v1;

import com.edu.admin.client.ResResult;
import com.edu.admin.client.user.api.UserClient;
import com.edu.admin.client.user.bean.dto.UserDto;
import com.edu.admin.client.user.bean.request.CreateUserRequest;
import com.edu.admin.client.user.bean.request.DelUserRequest;
import com.edu.admin.client.user.bean.request.QueryUserRequest;
import com.edu.admin.client.user.bean.request.UpdateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/web/v1/user")
public class UserController {

    @Autowired
    private UserClient userClient;

    /**
     * 保存用户信息
     *
     * @param createUserRequest 用户创建请求对象，包含用户的基本信息
     * @return ResResult<UserDto> 统一响应结果，包含保存成功的用户信息
     */
    @PostMapping(value = "/add")
    public ResResult<UserDto> saveCourse(@RequestBody CreateUserRequest createUserRequest) {
        UserDto response = userClient.save(createUserRequest);
        return ResResult.okResult(response);
    }

    /**
     * 编辑用户信息
     *
     * @param updateUserRequest 包含用户更新信息的请求对象
     * @return 返回更新后的用户信息结果
     */
    @PostMapping("/edit")
    public ResResult<UserDto> editCourse(@RequestBody UpdateUserRequest updateUserRequest) {
        UserDto response = userClient.update(updateUserRequest);
        return ResResult.okResult(response);
    }

    /**
     * 删除用户信息
     *
     * @param delUserRequest 包含用户删除信息的请求对象
     * @return 返回删除操作的结果
     */
    @GetMapping("/delete")
    public ResResult<UserDto> delete(DelUserRequest delUserRequest) {
        UserDto response = userClient.delete(delUserRequest);
        return ResResult.okResult(response);
    }

    /**
     * 查询用户详情
     *
     * @param queryUserRequest 包含用户查询条件的请求对象
     * @return 返回用户详细信息
     */
    @GetMapping("/detail")
    public ResResult<UserDto> details(QueryUserRequest queryUserRequest) {
        UserDto response = userClient.details(queryUserRequest);
        return ResResult.okResult(response);
    }

    /**
     * 分页查询用户列表
     *
     * @param queryUserRequest 包含分页查询条件的请求对象
     * @return 返回分页的用户信息列表
     */
    @GetMapping("/list")
    public ResResult<Page<UserDto>> pagingQuery(QueryUserRequest queryUserRequest) {
        Page<UserDto> response = userClient.pagingQuery(queryUserRequest);
        return ResResult.okResult(response);
    }
}
