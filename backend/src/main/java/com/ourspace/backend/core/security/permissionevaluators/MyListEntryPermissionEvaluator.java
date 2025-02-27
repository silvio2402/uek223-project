package com.ourspace.backend.core.security.permissionevaluators;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ourspace.backend.domain.mylistentry.MyListEntryService;
import com.ourspace.backend.domain.user.User;
import com.ourspace.backend.domain.user.UserUtil;

/**
 * Permission evaluator for MyListEntry.
 */
@Component
public class MyListEntryPermissionEvaluator {

  private final MyListEntryService myListEntryService;

  @Autowired
  public MyListEntryPermissionEvaluator(MyListEntryService myListEntryService) {
    this.myListEntryService = myListEntryService;
  }

  /**
   * Checks if the current user is the owner of the MyListEntry.
   *
   * @param uuid the UUID of the MyListEntry
   * @return true if the current user is the owner, false otherwise
   */
  public boolean isOwnEntry(UUID uuid) {
    User user = UserUtil.getCurrentUser();
    return myListEntryService.findById(uuid).getUser().getId().equals(user.getId());
  }
}
