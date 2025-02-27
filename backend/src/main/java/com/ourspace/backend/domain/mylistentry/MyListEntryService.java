package com.ourspace.backend.domain.mylistentry;

import com.ourspace.backend.core.generic.AbstractService;

/**
 * Service interface for MyListEntry.
 */
public interface MyListEntryService extends AbstractService<MyListEntry> {
  public MyListEntry create(MyListEntry myListEntry);
}
