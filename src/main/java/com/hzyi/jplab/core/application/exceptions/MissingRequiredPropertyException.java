package com.hzyi.jplab.core.application.exceptions;

public class MissingRequiredPropertyException extends IllegalArgumentException {

  private final String argument;

  public MissingRequiredPropertyException(String argument) {
    super();
    this.argument = argument;
  }

  @Override
  public String getMessage() {
    return String.format("missing property: %s", argument);
  }
}
