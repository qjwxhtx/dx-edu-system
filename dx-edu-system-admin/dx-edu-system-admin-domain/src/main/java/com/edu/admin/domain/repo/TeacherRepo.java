package com.edu.admin.domain.repo;

import com.edu.admin.domain.bean.TeacherDO;

public interface TeacherRepo {

    TeacherDO save(TeacherDO teacherDO);

    void addTeachersAge();
}
