package com.edu.auth.repository;

import com.edu.auth.entity.SRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 *
 * @author hjw
 * @since 2025-10-23
 */
@Repository
public interface SRoleRepository extends JpaRepository<SRoleEntity, Long> {

    @Query("SELECT r FROM SRoleEntity r WHERE r.id IN (SELECT ur.roleId FROM SUserRoleEntity ur WHERE ur.userId = :userId)")
    List<SRoleEntity> findByUserId(@Param("userId") Long userId);

}
