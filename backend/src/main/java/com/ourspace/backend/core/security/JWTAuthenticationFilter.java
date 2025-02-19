package com.ourspace.backend.core.security;

import com.ourspace.backend.core.security.helpers.AuthorizationSchemas;
import com.ourspace.backend.core.security.helpers.Credentials;
import com.ourspace.backend.core.security.helpers.JwtProperties;
import com.ourspace.backend.domain.user.UserDetailsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Log4j2
public class JWTAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  private final JwtProperties jwtProperties;

  public JWTAuthenticationFilter(RequestMatcher requestMatcher, AuthenticationManager authenticationManager,
      JwtProperties jwtProperties) {
    super(requestMatcher, authenticationManager);
    this.jwtProperties = jwtProperties;
  }

  private String generateToken(Authentication authResult) {
    UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authResult.getPrincipal();
    byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());

    return Jwts.builder()
        .claims(Map.of("sub", userDetailsImpl.user()
            .getId(), "authorities", userDetailsImpl.getAuthorities()))
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + jwtProperties.getExpirationMillis()))
        .issuer(jwtProperties.getIssuer())
        .signWith(Keys.hmacShaKeyFor(keyBytes))
        .compact();
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException {
    try {
      Credentials credentials = new ObjectMapper().readValue(request.getInputStream(), Credentials.class);
      return getAuthenticationManager().authenticate(
          new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPassword()));
    } catch (IOException e) {
      log.error("Exception while Authentication thrown.", e);
      return null;
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
      Authentication authResult) throws IOException {
    response.addHeader(HttpHeaders.AUTHORIZATION, AuthorizationSchemas.BEARER + " " + generateToken(authResult));
    UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authResult.getPrincipal();
    response.getWriter().write(new ObjectMapper().writeValueAsString(userDetailsImpl.user()));
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException failed) {
    SecurityContextHolder.clearContext();
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
  }
}
