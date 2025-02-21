package com.ourspace.backend.core.security.permissionevaluators;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.ourspace.backend.domain.user.User;
import com.ourspace.backend.domain.user.UserUtil;

@Component
public class UserPermissionEvaluator {

  public UserPermissionEvaluator() {
  }

  public boolean isOwnUser(UUID uuid) {
    User user = UserUtil.getCurrentUser();
    return user.getId().equals(uuid);
  }

}
