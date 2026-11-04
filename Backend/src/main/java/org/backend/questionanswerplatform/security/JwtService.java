package org.backend.questionanswerplatform.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {
    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration-ms}")
    private long expirationMs;

  private SecretKey key() {
      return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
  }

  public String generateToken(String email, String role)
  {
      Date now = new  Date();
      Date exp= new Date(now.getTime() + expirationMs);

      return Jwts.builder()
              .subject(email)
              .claim("role", role)
              .issuedAt(now)
              .expiration(exp)
              .signWith(key())
              .compact();
  }

  public String extractEmail(String token)
  {
      return parse(token).getSubject();
  }

  public String extractRole(String token)
  {
      return parse(token).get("role",String.class);
  }

  public Instant extractExpiration(String token)
  {
      return  parse(token).getExpiration().toInstant();
  }

    public boolean isValid(String token) {
        try {
            parse(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims parse(String token) {
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
