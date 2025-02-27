package com.ourspace.backend.domain.mylistentry.dto;

import java.util.UUID;

import com.ourspace.backend.core.generic.AbstractDTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MyListEntryDTO extends AbstractDTO {

  @NotBlank
  private String title;

  @NotBlank
  private String text;

  @Min(0)
  private Integer importance;

  private java.sql.Date creation_date;

  private UUID user_id;
}
