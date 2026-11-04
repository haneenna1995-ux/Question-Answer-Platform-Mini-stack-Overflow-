package org.backend.questionanswerplatform.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table (name="revoked_tokens")
public class RevokedToken {
    @Id
    @Column(length = 512)
    private String token;

    @Column(nullable = false)
    private Instant expiresAt;

    public RevokedToken() {
    }

    public RevokedToken(String token, Instant expiresAt) {
        this.token = token;
        this.expiresAt = expiresAt;
    }

    public String getToken() {
        return token;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }
}
