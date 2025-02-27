package com.ourspace.backend.core.exception;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Represents a generic error response with timestamp and error details.
 */
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class ResponseError implements Serializable {
  private LocalDate timeStamp;
  private Map<String, String> errors;

  public ResponseError build() {
    return this;
  }
}
