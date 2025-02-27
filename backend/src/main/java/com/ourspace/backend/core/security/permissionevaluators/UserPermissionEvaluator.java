package com.ourspace.backend.core.security.permissionevaluators;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.ourspace.backend.domain.user.User;
import com.ourspace.backend.domain.user.UserUtil;

/**
 * Permission evaluator for User.
 */
@Component
public class UserPermissionEvaluator {

  public UserPermissionEvaluator() {
  }

  /**
   * Checks if the current user is the same as the user being accessed.
   *
   * @param uuid the UUID of the user being accessed
   * @return true if the current user is the same, false otherwise
   */
  public boolean isOwnUser(UUID uuid) {
    User user = UserUtil.getCurrentUser();
    return user.getId().equals(uuid);
  }

}
