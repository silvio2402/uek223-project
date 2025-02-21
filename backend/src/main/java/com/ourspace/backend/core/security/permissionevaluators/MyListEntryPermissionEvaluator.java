package com.ourspace.backend.core.security.permissionevaluators;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ourspace.backend.domain.mylistentry.MyListEntryService;
import com.ourspace.backend.domain.user.User;
import com.ourspace.backend.domain.user.UserUtil;

@Component
public class MyListEntryPermissionEvaluator {

  private final MyListEntryService myListEntryService;

  @Autowired
  public MyListEntryPermissionEvaluator(MyListEntryService myListEntryService) {
    this.myListEntryService = myListEntryService;
  }

  public boolean isOwnEntry(UUID uuid) {
    User user = UserUtil.getCurrentUser();
    return myListEntryService.findById(uuid).getUser().getId().equals(user.getId());
  }
}
