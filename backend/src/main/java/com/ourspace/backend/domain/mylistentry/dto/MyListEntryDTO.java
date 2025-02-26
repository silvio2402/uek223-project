package com.ourspace.backend.domain.mylistentry.dto;

import java.util.Date;
import java.util.UUID;

import com.ourspace.backend.core.generic.AbstractDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MyListEntryDTO extends AbstractDTO {

  private String title;

  private String text;

  private Integer importance;

  private Date creation_date;

  private UUID user_id;
}
