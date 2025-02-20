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

@Service
public class JwtUtil {
  @Value("${jwt.secret}")
  private String secretKey;

  @Value("${jwt.access-token.expiration}")
  private long jwtExpiration;

  @Value("${jwt.refresh-token.expiration}")
  private long refreshExpiration;

  public String generateToken(String userId, Collection<? extends GrantedAuthority> authorities) {
    return buildToken(userId, authorities, jwtExpiration);
  }

  public String generateRefreshToken(String userId, Collection<? extends GrantedAuthority> authorities) {
    return buildToken(userId, authorities, refreshExpiration);
  }

  public String extractUserId(String token) {
    return Jwts
        .parser()
        .verifyWith(getSecretKey())
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
  }

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
