package com.ourspace.backend.domain.mylistentry;

import com.ourspace.backend.core.generic.AbstractRepository;
import com.ourspace.backend.core.generic.AbstractServiceImpl;
import com.ourspace.backend.domain.user.User;

public class MyListEntryServiceImpl extends AbstractServiceImpl<MyListEntry> implements MyListEntryService {

  public MyListEntryServiceImpl(AbstractRepository<MyListEntry> repository) {
    super(repository);
  }

  @Override
  public MyListEntry create(MyListEntry myListEntry, User user) {
    myListEntry.setUser(user);
    return save(myListEntry);
  }
}
