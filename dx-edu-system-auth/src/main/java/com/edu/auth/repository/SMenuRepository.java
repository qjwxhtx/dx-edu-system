package com.edu.auth.repository;

import com.edu.auth.entity.SMenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 菜单repository
 *
 * @author hjw
 * @since 2025-10-27
 */
@Repository
public interface SMenuRepository extends JpaRepository<SMenuEntity, Long> {

    @Query(
        "SELECT m FROM SMenuEntity m WHERE m.id IN " + "(SELECT rm.menuId FROM SRoleMenuEntity rm WHERE rm.roleId IN "
            + "(SELECT ur.roleId FROM SUserRoleEntity ur WHERE ur.userId = :userId)) "
            + "AND m.status = 1 ORDER BY m.sortOrder")
    List<SMenuEntity> findMenusByUserId(@Param("userId") Long userId);

}
