package com.edu.admin.infra.repoimpl;


import com.edu.admin.domain.bean.UserDo;
import com.edu.admin.domain.repo.UserRepo;
import com.edu.admin.infra.bean.entity.UserEntity;
import com.edu.admin.infra.convertor.UserInfraConvertor;
import com.edu.admin.infra.repositories.UserRepository;
import com.edu.admin.infra.utils.PredicateBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component("UserRepoImpl")
public class UserRepoImpl implements UserRepo {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDo save(UserDo userDo) {
        UserEntity user = UserInfraConvertor.convertDoToEntity(userDo);
        UserEntity save = userRepository.save(user);
        return UserInfraConvertor.convertEntityToDo(save);
    }

    @Override
    public UserDo update(UserDo userDo) {
        UserEntity user = UserInfraConvertor.convertDoToEntity(userDo);
        UserEntity save = userRepository.saveAndFlush(user);
        return UserInfraConvertor.convertEntityToDo(save);
    }

    @Override
    public UserDo delete(UserDo userDo) {
        userRepository.delete(UserInfraConvertor.convertDoToEntity(userDo));
        return null;
    }

    @Override
    public UserDo detail(UserDo userDo) {
        UserEntity byId = userRepository.findById(userDo.getId()).get();
        return UserInfraConvertor.convertEntityToDo(byId);
    }

    @Override
    public Page<UserDo> pageList(UserDo cmd) {
        PageRequest pageRequest =
            PageRequest.of(cmd.getPageNum() - 1, cmd.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
        Specification<UserEntity> specification = getSpec(cmd);
        Page<UserEntity> page = userRepository.findAll(specification, pageRequest);
        return page.map(UserInfraConvertor::convertEntityToDo);
    }

    private Specification<UserEntity> getSpec(UserDo cmd) {
        return (root, query, criteriaBuilder) -> {
            PredicateBuilder builder = PredicateBuilder.builder(root, criteriaBuilder);
            if (cmd.getName() != null && !cmd.getName().isEmpty()) {
                builder.likeBilateral("name", "%" + cmd.getName() + "%");
            }
            return builder.build();
        };
    }
}
