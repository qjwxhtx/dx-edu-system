package com.edu.admin.client.course.dubbo;

import com.edu.admin.client.user.bean.dto.UserDto;
import com.edu.admin.client.user.bean.request.QueryUserRequest;

public interface UserClientDubbo {

    UserDto detailsDubbo(QueryUserRequest queryUserRequest);

}
