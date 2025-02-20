package com.ourspace.backend.domain.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

@Validated
@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;
  private final AuthenticationManager authenticationManager;

  @Autowired
  public AuthController(AuthService authService, AuthenticationManager authenticationManager) {
    this.authService = authService;
    this.authenticationManager = authenticationManager;
  }

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

  @PostMapping("/refresh")
  public ResponseEntity<AuthResponse> refresh() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    UserDetails userDetails = (UserDetails) auth.getPrincipal();

    return ResponseEntity.ok(authService.generateTokens(userDetails));
  }
}
