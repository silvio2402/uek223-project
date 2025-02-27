package com.ourspace.backend.domain.mylistentry;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ourspace.backend.core.generic.AbstractRepository;
import com.ourspace.backend.core.generic.AbstractServiceImpl;
import com.ourspace.backend.domain.user.User;
import com.ourspace.backend.domain.user.UserUtil;

/**
 * Service implementation for MyListEntry.
 */
@Service
public class MyListEntryServiceImpl extends AbstractServiceImpl<MyListEntry> implements MyListEntryService {

  public MyListEntryServiceImpl(AbstractRepository<MyListEntry> repository) {
    super(repository);
  }

  /**
   * Creates a new MyListEntry.
   *
   * @param myListEntry the MyListEntry to create
   * @return the created MyListEntry
   */
  @Override
  public MyListEntry create(MyListEntry myListEntry) {
    User user = UserUtil.getCurrentUser();
    myListEntry.setCreation_date(new java.sql.Date(new Date().getTime()));
    myListEntry.setUser(user);
    return save(myListEntry);
  }

  /**
   * Updates an existing MyListEntry by ID.
   *
   * @param id     the ID of the MyListEntry to update
   * @param entity the updated MyListEntry
   * @return the updated MyListEntry
   * @throws NoSuchElementException if the MyListEntry is not found
   */
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
