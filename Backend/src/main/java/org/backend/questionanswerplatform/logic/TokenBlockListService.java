package org.backend.questionanswerplatform.logic;

import org.backend.questionanswerplatform.entities.RevokedToken;
import org.backend.questionanswerplatform.repositories.RevokedTokenRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenBlockListService {
    private final RevokedTokenRepository revokedTokenRepository;

    public TokenBlockListService(RevokedTokenRepository revokedTokenRepository)
    {
        this.revokedTokenRepository=revokedTokenRepository;

    }

    public boolean isRevoked(String toke)
    {
        return revokedTokenRepository.existsByToken(toke);
    }

    public void revoke(String token, Instant expiresAt) {
        revokedTokenRepository.save(new RevokedToken(token, expiresAt));
    }
}
