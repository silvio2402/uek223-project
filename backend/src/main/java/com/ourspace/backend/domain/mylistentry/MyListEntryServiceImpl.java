package com.ourspace.backend.domain.mylistentry;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ourspace.backend.core.generic.AbstractRepository;
import com.ourspace.backend.core.generic.AbstractServiceImpl;
import com.ourspace.backend.domain.mylistentry.dto.MyListEntryMapper;
import com.ourspace.backend.domain.mylistentry.dto.PostMyListEntryDTO;
import com.ourspace.backend.domain.user.User;
import com.ourspace.backend.domain.user.UserUtil;

@Service
public class MyListEntryServiceImpl extends AbstractServiceImpl<MyListEntry> implements MyListEntryService {

  private final MyListEntryMapper myListEntryMapper;

  @Autowired
  public MyListEntryServiceImpl(AbstractRepository<MyListEntry> repository, MyListEntryMapper myListEntryMapper) {
    super(repository);
    this.myListEntryMapper = myListEntryMapper;
  }

  @Override
  public MyListEntry create(MyListEntry myListEntry) {
    User user = UserUtil.getCurrentUser();
    myListEntry.setCreation_date(new java.sql.Date(new Date().getTime()));
    myListEntry.setUser(user);
    return save(myListEntry);
  }
}
