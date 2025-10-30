package com.edu.admin.infra.repoimpl;


import com.edu.admin.domain.bean.TeacherDO;
import com.edu.admin.domain.repo.TeacherRepo;
import com.edu.admin.infra.bean.entity.TeacherEntity;
import com.edu.admin.infra.convertor.TeacherInfraConvertor;
import com.edu.admin.infra.repositories.TeacherRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 教师repo实现
 *
 * @author wangzhan
 * @since 2025-10-24
 */
@Repository
@Slf4j
public class TeacherRepoImpl implements TeacherRepo {
    @Autowired
    private TeacherRepository teacherRepository;

    @Override
    public TeacherDO save(TeacherDO teacherDO) {
        TeacherEntity teacherEntity = TeacherInfraConvertor.doToEntity(teacherDO);
        TeacherEntity entity = teacherRepository.save(teacherEntity);
        return TeacherInfraConvertor.entityToDo(entity);
    }

    @Override
    public void addTeachersAge() {
        List<TeacherEntity> teacherEntities = teacherRepository.findAll();
        teacherEntities.forEach(teacherEntity -> {
            teacherEntity.setAge(teacherEntity.getAge() + 1);
        });
        teacherRepository.saveAll(teacherEntities);


//        try {
//            log.info("开始执行批量更新");
//            teacherRepository.batchUpdateAge();
//            log.info("更新完成，事务将提交");
//        } catch (Exception e) {
//            log.error("更新失败，事务将回滚", e); // 打印异常，确认是否回滚
//            throw e; // 必须抛出异常，否则事务可能不回滚
//        }
////        log.info("开始执行批量更新");
////        teacherRepository.batchUpdateAge();
////        log.info("开始执行批量更新-hou");

    }
}
