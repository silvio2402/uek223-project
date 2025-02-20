package com.ourspace.backend.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AuthResponse{
  private String accessToken;
  private String refreshToken;
}
