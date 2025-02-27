package com.ourspace.backend.domain.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ourspace.backend.domain.auth.dto.AuthRequest;
import com.ourspace.backend.domain.auth.dto.AuthResponse;

import jakarta.validation.Valid;

/**
 * Controller for authentication endpoints.
 */
@Validated
@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;
  private final AuthenticationManager authenticationManager;

  public AuthController(AuthService authService, AuthenticationManager authenticationManager) {
    this.authService = authService;
    this.authenticationManager = authenticationManager;
  }

  /**
   * Authenticates a user and generates tokens.
   *
   * @param request the authentication request
   * @return the authentication response
   */
  @PostMapping("/authenticate")
  public ResponseEntity<AuthResponse> authenticate(@Valid @RequestBody AuthRequest request) {
    Authentication auth = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(auth);

    UserDetails userDetails = (UserDetails) auth.getPrincipal();

    return ResponseEntity.ok(authService.generateTokens(userDetails));
  }

  /**
   * Refreshes the tokens for a user.
   *
   * @return the authentication response
   */
  @PostMapping("/refresh")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<AuthResponse> refresh() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    UserDetails userDetails = (UserDetails) auth.getPrincipal();

    return ResponseEntity.ok(authService.generateTokens(userDetails));
  }
}
