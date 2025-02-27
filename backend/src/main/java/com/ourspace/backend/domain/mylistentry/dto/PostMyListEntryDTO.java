package com.ourspace.backend.domain.mylistentry.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostMyListEntryDTO {

  @NotNull
  private String title;

  @NotNull
  private String text;

  @Min(0)
  private Integer importance;
}
