package com.ourspace.backend.core.security.permissionevaluators;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.ourspace.backend.domain.mylistentry.MyListEntryService;
import com.ourspace.backend.domain.user.User;
import com.ourspace.backend.domain.user.UserUtil;

@Component
public class MyListEntryPermissionEvaluator {
  public MyListEntryPermissionEvaluator() {
  }

  public boolean isOwnEntry(UUID uuid, MyListEntryService service) {
    User user = UserUtil.getCurrentUser();
    return service.findById(uuid).getUser().getId() == user.getId();
  }
}
