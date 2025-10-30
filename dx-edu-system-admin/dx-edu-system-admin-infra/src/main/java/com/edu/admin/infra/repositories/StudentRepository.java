package com.edu.admin.infra.repositories;

import com.edu.admin.infra.bean.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 学员Repository
 *
 * @author hjw
 * @since 2025-10-22
 */
public interface StudentRepository extends JpaRepository<StudentEntity, Long>, JpaSpecificationExecutor<StudentEntity> {

    StudentEntity findByStudentCode(String studentCode);

    void deleteByStudentCode(String studentCode);

}
