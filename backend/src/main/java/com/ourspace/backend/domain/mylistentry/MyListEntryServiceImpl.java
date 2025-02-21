package com.ourspace.backend.domain.mylistentry;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ourspace.backend.core.generic.AbstractRepository;
import com.ourspace.backend.core.generic.AbstractServiceImpl;
import com.ourspace.backend.domain.user.User;
import com.ourspace.backend.domain.user.UserUtil;

@Service
public class MyListEntryServiceImpl extends AbstractServiceImpl<MyListEntry> implements MyListEntryService {

  @Autowired
  public MyListEntryServiceImpl(AbstractRepository<MyListEntry> repository) {
    super(repository);
  }

  @Override
  public MyListEntry create(MyListEntry myListEntry) {
    User user = UserUtil.getCurrentUser();
    myListEntry.setCreation_date(new java.sql.Date(new Date().getTime()));
    myListEntry.setUser(user);
    return save(myListEntry);
  }

  @Override
  public MyListEntry updateById(UUID id, MyListEntry entity) throws NoSuchElementException {
    if (repository.existsById(id)) {
      entity.setId(id);
      return repository.save(entity);
    } else {
      throw new NoSuchElementException(String.format("Entity with ID '%s' could not be found", id));
    }
  }
}
