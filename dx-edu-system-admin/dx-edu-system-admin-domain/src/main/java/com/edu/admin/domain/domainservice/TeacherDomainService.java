package com.edu.admin.domain.domainservice;


import com.edu.admin.domain.bean.TeacherDO;
import com.edu.admin.domain.repo.TeacherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 教师domainService
 *
 * @author wangzhan
 * @since 2025-10-24
 */
@Service
public class TeacherDomainService {

    @Autowired
    private TeacherRepo teacherRepo;

    public TeacherDO save(TeacherDO teacherDO) {
        return teacherRepo.save(teacherDO);
    }

    public void addTeachersAge() {
        teacherRepo.addTeachersAge();
    }
}
