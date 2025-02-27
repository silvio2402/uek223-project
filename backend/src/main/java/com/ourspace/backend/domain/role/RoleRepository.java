package com.ourspace.backend.domain.role;

import org.springframework.stereotype.Repository;

import com.ourspace.backend.core.generic.AbstractRepository;

/**
 * Repository for Role.
 */
@Repository
public interface RoleRepository extends AbstractRepository<Role> {
  Role findByName(String name);
}
