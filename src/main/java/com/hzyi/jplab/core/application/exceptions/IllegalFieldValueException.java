package com.hzyi.jplab.core.application.exceptions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class IllegalFieldValueException extends IllegalArgumentException {

  private final String field;
  private final String reason;

  @Override
  public String getMessage() {
    return String.format("field %s has invalid value: %s", field, reason);
  }
}
