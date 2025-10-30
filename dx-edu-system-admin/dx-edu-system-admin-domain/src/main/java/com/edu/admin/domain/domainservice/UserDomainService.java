package com.edu.admin.domain.domainservice;


import com.edu.admin.domain.bean.UserDo;
import com.edu.admin.domain.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDomainService {

    private final UserRepo userRepo;

    public UserDo save(UserDo userDo) {
        return userRepo.save(userDo);
    }

    public UserDo delete(UserDo userDo) {
        return userRepo.delete(userDo);
    }

    public UserDo detail(UserDo userDo) {
        return userRepo.detail(userDo);
    }

    public UserDo update(UserDo userDo) {
        return userRepo.update(userDo);
    }
}
