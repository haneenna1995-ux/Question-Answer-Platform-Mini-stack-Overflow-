package org.backend.questionanswerplatform.repositories;

import jakarta.transaction.Transactional;
import org.backend.questionanswerplatform.entities.RevokedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;

public interface RevokedTokenRepository extends JpaRepository<RevokedToken,String> {
    boolean existsByToken(String token);
    @Modifying
    @Transactional
    @Query("DELETE FROM RevokedToken r WHERE r.expiresAt<:now")
    void deleteExpired(Instant now);

}
