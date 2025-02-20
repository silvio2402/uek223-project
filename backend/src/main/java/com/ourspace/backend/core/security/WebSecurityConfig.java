package com.ourspace.backend.core.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.ourspace.backend.core.security.helpers.JwtUtil;
import com.ourspace.backend.domain.user.UserService;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

  private final UserService userService;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;

  @Autowired
  public WebSecurityConfig(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
    this.jwtUtil = jwtUtil;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
        .authorizeHttpRequests(
            requests -> requests
                .requestMatchers(HttpMethod.POST, "/auth/authenticate", "/user/register", "/error")
                .permitAll()
                .requestMatchers(HttpMethod.GET, "/v3/api-docs", "/v3/api-docs/swagger-config", "/swagger-ui/*")
                .permitAll().anyRequest().authenticated())
        .addFilterAfter(new JWTAuthorizationFilter(userService, jwtUtil), UsernamePasswordAuthenticationFilter.class)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(csrf -> csrf.disable())
        .build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("*"));
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
    configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
    configuration.setExposedHeaders(List.of("Authorization"));
    UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
    configurationSource.registerCorsConfiguration("/**", configuration);
    return configurationSource;
  }

  @Bean
  public AuthenticationManager authenticationManager() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(passwordEncoder);
    provider.setUserDetailsService(userService);
    return new ProviderManager(provider);
  }

}
