package com.ourspace.backend.domain.authority;

import java.util.UUID;

import com.ourspace.backend.core.generic.AbstractEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Entity representing an authority.
 */
@Entity
@Table(name = "authority")
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class Authority extends AbstractEntity {

  @Column(name = "name", nullable = false, unique = true)
  private String name;

  public Authority(UUID id, String name) {
    super(id);
    this.name = name;
  }
}
