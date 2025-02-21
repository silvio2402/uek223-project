package com.ourspace.backend.domain.user;

import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtil {

  public static User getCurrentUser() {
    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
    return userDetails.getUser();
  }
}
