package com.ourspace.backend.domain.mylistentry.dto;

import java.util.Date;
import java.util.UUID;

import com.ourspace.backend.core.generic.AbstractDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class MyListEntryDTO extends AbstractDTO {

  private String title;

  private String text;

  private String importance;

  private Date creation_date;

  private UUID user_id;

  public MyListEntryDTO(UUID id, String title, String text, String importance, Date creation_date, UUID user_id) {
    super(id);
    this.title = title;
    this.text = text;
    this.importance = importance;
    this.creation_date = creation_date;
    this.user_id = user_id;
  }
}
