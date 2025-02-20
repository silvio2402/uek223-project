package com.ourspace.backend.core.security;

import java.io.IOException;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ourspace.backend.core.security.helpers.AuthorizationSchemas;
import com.ourspace.backend.core.security.helpers.JwtUtil;
import com.ourspace.backend.domain.user.UserDetailsImpl;
import com.ourspace.backend.domain.user.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTAuthorizationFilter extends OncePerRequestFilter {

  private final UserService userService;
  private final JwtUtil jwtUtil;

  public JWTAuthorizationFilter(UserService userService, JwtUtil jwtUtil) {
    this.userService = userService;
    this.jwtUtil = jwtUtil;
  }

  private String resolveToken(String token) {
    if (token != null && token.startsWith(AuthorizationSchemas.BEARER.toString())) {
      return jwtUtil.extractUserId(token.replace(AuthorizationSchemas.BEARER + " ", ""));
    } else {
      return null;
    }
  }

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    try {
      String authToken = request.getHeader(HttpHeaders.AUTHORIZATION);
      UserDetails userDetails = new UserDetailsImpl(userService.findById(UUID.fromString(resolveToken(authToken))));
      SecurityContextHolder.getContext()
          .setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null,
              userDetails.getAuthorities()));
    } catch (RuntimeException e) {
      SecurityContextHolder.clearContext();
    }
    filterChain.doFilter(request, response);
  }
}
