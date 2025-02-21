package com.ourspace.backend.core.security.permissionevaluators;

import org.springframework.stereotype.Component;

import com.ourspace.backend.domain.user.User;
import com.ourspace.backend.domain.user.UserUtil;

@Component
public class UserPermissionEvaluator {

  public UserPermissionEvaluator() {
  }

  public boolean isOwnUser(User user) {
    User activeUser = UserUtil.getCurrentUser();
    return user.getId() == activeUser.getId();
  }

}
