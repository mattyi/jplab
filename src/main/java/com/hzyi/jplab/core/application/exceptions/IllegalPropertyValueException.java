package com.hzyi.jplab.core.application.exceptions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class IllegalPropertyValueException extends IllegalArgumentException {

  private final String property;
  private final String reason;

  @Override
  public String getMessage() {
    return String.format("property %s has invalid value: %s", property, reason);
  }
}
