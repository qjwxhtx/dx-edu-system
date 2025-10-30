package com.edu.perm.infra.repositories;

import com.edu.perm.infra.bean.entity.SRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 *
 * @author hjw
 * @since 2025-10-23
 */
@Repository
public interface SRoleRepository extends JpaRepository<SRoleEntity, Long> {

}
