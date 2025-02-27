package com.ourspace.backend.domain.auth;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.ourspace.backend.core.security.helpers.JwtUtil;
import com.ourspace.backend.domain.auth.dto.AuthResponse;
import com.ourspace.backend.domain.user.UserDetailsImpl;

/**
 * Service implementation for authentication.
 */
@Service
public class AuthServiceImpl implements AuthService {
  private final JwtUtil jwtUtil;

  public AuthServiceImpl(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  /**
   * Generates access and refresh tokens for the given user details.
   *
   * @param userDetails the user details
   * @return the authentication response
   */
  @Override
  public AuthResponse generateTokens(UserDetails userDetails) {
    UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;
    String userId = userDetailsImpl.user().getId().toString();
    Collection<? extends GrantedAuthority> authorities = userDetailsImpl.getAuthorities();
    var accessToken = jwtUtil.generateToken(userId, authorities);
    var refreshToken = jwtUtil.generateRefreshToken(userId, authorities);
    return new AuthResponse(accessToken, refreshToken);
  }
}
