package com.ourspace.backend.domain.mylistentry;

import com.ourspace.backend.core.generic.AbstractService;
import com.ourspace.backend.domain.user.User;

public interface MyListEntryService extends AbstractService<MyListEntry> {
  public MyListEntry create(MyListEntry myListEntry, User user);
}
