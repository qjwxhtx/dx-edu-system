package com.edu.admin.infra.repositories;

import com.edu.admin.infra.bean.entity.TeacherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author wangzhan
 * @since 2025-10-24
 */
public interface TeacherRepository extends JpaRepository<TeacherEntity, Long>, JpaSpecificationExecutor<TeacherEntity> {
    // 自定义批量更新方法：所有教师年龄+1
    @Modifying
    @Query("UPDATE TeacherEntity t SET t.age = t.age + 1")
    void batchUpdateAge();
}
