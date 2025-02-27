package com.ourspace.backend.domain.user.dto;

import java.util.Set;

import com.ourspace.backend.core.generic.AbstractDTO;
import com.ourspace.backend.domain.role.dto.RoleDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class UserDTO extends AbstractDTO {

  @NotBlank
  private String firstName;

  @NotBlank
  private String lastName;

  @Email
  private String email;

  @Valid
  private Set<RoleDTO> roles;

}
