package com.ourspace.backend.domain.role;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.ourspace.backend.core.generic.AbstractEntity;
import com.ourspace.backend.domain.authority.Authority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Entity representing a role.
 */
@Entity
@Table(name = "role")
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class Role extends AbstractEntity {

  @Column(name = "name", nullable = false, unique = true)
  private String name;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "role_authority", joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
  private Set<Authority> authorities = new HashSet<>();

  public Role(UUID id, String name, Set<Authority> authorities) {
    super(id);
    this.name = name;
    this.authorities = authorities;
  }

}
