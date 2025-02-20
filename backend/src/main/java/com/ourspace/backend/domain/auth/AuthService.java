package com.ourspace.backend.domain.auth;

import org.springframework.security.core.userdetails.UserDetails;

import com.ourspace.backend.domain.auth.dto.AuthResponse;

public interface AuthService {
  AuthResponse generateTokens(UserDetails userDetails);
}
