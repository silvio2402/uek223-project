package com.ourspace.backend.domain.user;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.ourspace.backend.core.generic.AbstractService;

/**
 * Service interface for User.
 */
public interface UserService extends UserDetailsService, AbstractService<User> {
  User register(User user);

  User registerUser(User user);
}
