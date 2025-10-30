package com.edu.admin.infra.repositories;

import com.edu.admin.infra.bean.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author qianwj
 * @since 2025-10-15
 */
public interface CourseRepository extends JpaRepository<CourseEntity, Long>, JpaSpecificationExecutor<CourseEntity> {

}
