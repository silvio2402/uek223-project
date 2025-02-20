package com.ourspace.backend.core.security.permissionevaluators;

import org.springframework.stereotype.Component;

import com.ourspace.backend.domain.user.User;

@Component
public class UserPermissionEvaluator {

  public UserPermissionEvaluator() {
  }

  public boolean isOwnUser(User principal, User user) {
    return user.getId() == principal.getId();
  }

}
