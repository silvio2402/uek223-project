package com.ourspace.backend.domain.role.dto;

import java.util.Set;
import java.util.UUID;

import com.ourspace.backend.core.generic.AbstractDTO;
import com.ourspace.backend.domain.authority.dto.AuthorityDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * DTO for Role.
 */
@NoArgsConstructor
@Setter
@Getter
@Accessors(chain = true)
public class RoleDTO extends AbstractDTO {

  @NotNull
  @Size(min = 1, max = 255)
  private String name;

  @Valid
  private Set<AuthorityDTO> authorities;

  public RoleDTO(UUID id, String name, Set<AuthorityDTO> authorities) {
    super(id);
    this.name = name;
    this.authorities = authorities;
  }

}
