package com.ourspace.backend.domain.mylistentry;

import com.ourspace.backend.core.generic.AbstractService;
import com.ourspace.backend.domain.mylistentry.dto.PostMyListEntryDTO;

public interface MyListEntryService extends AbstractService<MyListEntry> {
  public MyListEntry create(PostMyListEntryDTO myListEntry);
}
