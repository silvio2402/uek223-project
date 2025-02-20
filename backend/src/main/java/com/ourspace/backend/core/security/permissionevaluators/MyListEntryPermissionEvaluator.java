package com.ourspace.backend.core.security.permissionevaluators;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.ourspace.backend.domain.mylistentry.MyListEntryService;
import com.ourspace.backend.domain.user.User;

@Component
public class MyListEntryPermissionEvaluator {
  public MyListEntryPermissionEvaluator() {
  }

  public boolean isOwnEntry(User principal, UUID uuid, MyListEntryService service) {
    return service.findById(uuid).getUser().getId() == principal.getId();
  }
}
