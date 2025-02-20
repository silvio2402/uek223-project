package com.ourspace.backend.core.security.permissionevaluators;

import org.springframework.stereotype.Component;

import com.ourspace.backend.domain.user.User;

@Component
public class UserPermissionEvaluator {

  public UserPermissionEvaluator() {
  }

  public boolean isUserAboveAge(User principal, int age) {
    return true;
  }

}
