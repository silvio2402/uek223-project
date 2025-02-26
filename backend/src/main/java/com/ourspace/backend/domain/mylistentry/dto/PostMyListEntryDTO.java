package com.ourspace.backend.domain.mylistentry.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostMyListEntryDTO {

  private String title;

  private String text;

  private String importance;
}
