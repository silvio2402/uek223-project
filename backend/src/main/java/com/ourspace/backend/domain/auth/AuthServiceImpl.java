package com.ourspace.backend.domain.auth;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.ourspace.backend.core.security.helpers.JwtUtil;
import com.ourspace.backend.domain.auth.dto.AuthResponse;
import com.ourspace.backend.domain.user.UserDetailsImpl;

@Service
public class AuthServiceImpl implements AuthService {
  private final JwtUtil jwtUtil;

  @Autowired
  public AuthServiceImpl(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

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
