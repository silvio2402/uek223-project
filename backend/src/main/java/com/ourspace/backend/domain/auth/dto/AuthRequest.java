package com.ourspace.backend.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for authentication request.
 */
@Getter
@Setter
@Builder
public class AuthRequest {
  @NotBlank
  private String email;
  @NotBlank
  private String password;
}
