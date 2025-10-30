package com.edu.perm.infra.repositories;

import com.edu.perm.infra.bean.entity.SMenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 菜单repository
 *
 * @author hjw
 * @since 2025-10-27
 */
@Repository
public interface SMenuRepository extends JpaRepository<SMenuEntity, Long> {

}
