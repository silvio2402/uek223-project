package com.ourspace.backend.domain.user;

import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utility class for User.
 */
public class UserUtil {

  /**
   * Gets the current user from the security context.
   *
   * @return the current user
   */
  public static User getCurrentUser() {
    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
    return userDetails.getUser();
  }
}
