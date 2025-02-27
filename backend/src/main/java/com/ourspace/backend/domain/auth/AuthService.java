package com.ourspace.backend.domain.auth;

import org.springframework.security.core.userdetails.UserDetails;

import com.ourspace.backend.domain.auth.dto.AuthResponse;

/**
 * Service interface for authentication.
 */
public interface AuthService {
  AuthResponse generateTokens(UserDetails userDetails);
}
