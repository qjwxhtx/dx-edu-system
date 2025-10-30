package com.edu.admin.domain.repo;

import com.edu.admin.domain.bean.UserDo;
import org.springframework.data.domain.Page;

public interface UserRepo {

    UserDo save(UserDo userDo);

    UserDo update(UserDo userDo);

    UserDo delete(UserDo userDo);

    UserDo detail(UserDo userDo);

    Page<UserDo> pageList(UserDo userDo);
}
