package com.edu.auth.repository;

import com.edu.auth.entity.SUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 *
 *
 * @author hjw
 * @since 2025-10-23
 */
@Repository
public interface SUserRepository extends JpaRepository<SUserEntity, Long> {

    Optional<SUserEntity> findByUsernameAndStatus(String username, Integer status);

    Optional<SUserEntity> findByUsernameAndTenantIdAndStatus(String username, Long tenantId, Integer status);

    boolean existsByUsernameAndTenantId(String username, Long tenantId);

    @Query("SELECT u FROM SUserEntity u WHERE u.id = :id AND u.tenantId = :tenantId")
    Optional<SUserEntity> findByIdAndTenantId(Long id, Long tenantId);

}
