package com.ourspace.backend.domain.mylistentry.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PostMyListEntryDTO {

  private String title;

  private String text;

  private String importance;
}
