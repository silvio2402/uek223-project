package com.ourspace.backend.domain.mylistentry;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ourspace.backend.core.generic.AbstractRepository;
import com.ourspace.backend.core.generic.AbstractServiceImpl;
import com.ourspace.backend.domain.mylistentry.dto.MyListEntryMapper;
import com.ourspace.backend.domain.mylistentry.dto.PostMyListEntryDTO;
import com.ourspace.backend.domain.user.User;

@Service
public class MyListEntryServiceImpl extends AbstractServiceImpl<MyListEntry> implements MyListEntryService {

  private final MyListEntryMapper myListEntryMapper;

  @Autowired
  public MyListEntryServiceImpl(AbstractRepository<MyListEntry> repository, MyListEntryMapper myListEntryMapper) {
    super(repository);
    this.myListEntryMapper = myListEntryMapper;
  }

  @Override
  public MyListEntry create(PostMyListEntryDTO myListEntryDTO, User user) {
    MyListEntry myListEntry = myListEntryMapper.toEntity(myListEntryDTO);
    myListEntry.setCreation_date(new java.sql.Date(new Date().getTime()));
    myListEntry.setUser(user);
    return save(myListEntry);
  }
}
