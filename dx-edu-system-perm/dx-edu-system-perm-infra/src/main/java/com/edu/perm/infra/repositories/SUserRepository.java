package com.edu.perm.infra.repositories;

import com.edu.perm.infra.bean.entity.SUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 *
 * @author hjw
 * @since 2025-10-23
 */
@Repository
public interface SUserRepository extends JpaRepository<SUserEntity, Long> {


}
