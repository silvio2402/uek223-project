package com.ourspace.backend.domain.user;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ourspace.backend.core.generic.AbstractRepository;

/**
 * Repository for User.
 */
@Repository
public interface UserRepository extends AbstractRepository<User> {
  Optional<User> findByEmail(String email);
}
