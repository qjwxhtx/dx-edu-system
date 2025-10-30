package com.edu.auth.repository;

import com.edu.auth.entity.Oauth2Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 *
 *
 * @author hjw
 * @since 2025-10-28
 */
@Repository
public interface Oauth2ClientRepository extends JpaRepository<Oauth2Client, String> {

    Optional<Oauth2Client> findByClientId(String clientId);

    boolean existsByClientId(String clientId);

    @Query("SELECT c FROM Oauth2Client c WHERE c.clientId = :clientId")
    Optional<Oauth2Client> findClientByClientId(@Param("clientId") String clientId);

}
