package com.edu.admin.client.user.api;

import com.edu.admin.client.user.bean.dto.UserDto;
import com.edu.admin.client.user.bean.request.CreateUserRequest;
import com.edu.admin.client.user.bean.request.DelUserRequest;
import com.edu.admin.client.user.bean.request.QueryUserRequest;
import com.edu.admin.client.user.bean.request.UpdateUserRequest;
import org.springframework.data.domain.Page;

public interface UserClient {
    UserDto save(CreateUserRequest createUserRequest);

    UserDto update(UpdateUserRequest updateUserRequest);

    UserDto delete(DelUserRequest delUserRequest);

    UserDto details(QueryUserRequest queryUserRequest);

    Page<UserDto> pagingQuery(QueryUserRequest queryUserRequest);
}
