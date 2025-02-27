package com.ourspace.backend.core.security.helpers;

import java.util.Collection;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * Utility class for handling JWT operations.
 */
@Service
public class JwtUtil {
  @Value("${jwt.secret}")
  private String secretKey;

  @Value("${jwt.access-token.expiration}")
  private long jwtExpiration;

  @Value("${jwt.refresh-token.expiration}")
  private long refreshExpiration;

  /**
   * Generates a JWT token.
   *
   * @param userId      the user ID
   * @param authorities the authorities
   * @return the JWT token
   */
  public String generateToken(String userId, Collection<? extends GrantedAuthority> authorities) {
    return buildToken(userId, authorities, jwtExpiration);
  }

  /**
   * Generates a refresh token.
   *
   * @param userId      the user ID
   * @param authorities the authorities
   * @return the refresh token
   */
  public String generateRefreshToken(String userId, Collection<? extends GrantedAuthority> authorities) {
    return buildToken(userId, authorities, refreshExpiration);
  }

  /**
   * Extracts the user ID from the token.
   *
   * @param token the token
   * @return the user ID
   */
  public String extractUserId(String token) {
    return Jwts
        .parser()
        .verifyWith(getSecretKey())
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
  }

  /**
   * Builds a JWT token.
   *
   * @param userId      the user ID
   * @param authorities the authorities
   * @param expiration  the expiration time
   * @return the JWT token
   */
  private String buildToken(
      String userId,
      Collection<? extends GrantedAuthority> authorities,
      long expiration) {
    return Jwts
        .builder()
        .claim("authorities", authorities)
        .subject(userId)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + expiration * 1000))
        .signWith(getSecretKey())
        .compact();
  }

  private SecretKey getSecretKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

}
