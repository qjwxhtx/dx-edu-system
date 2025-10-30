package com.edu.admin.domain.repo;

import com.edu.admin.domain.bean.StudentDo;

/**
 * 学员Repo
 *
 * @author hjw
 * @since 2025-10-22
 */
public interface StudentRepo {

    StudentDo findByCode(String code);

    void save(StudentDo studentDo);

    void update(StudentDo studentDo);

    void deleteByCode(String code);

}
