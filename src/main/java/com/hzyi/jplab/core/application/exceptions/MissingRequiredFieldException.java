package com.hzyi.jplab.core.application.exceptions;

public class MissingRequiredFieldException extends IllegalArgumentException {

  private final String argument;

  public MissingRequiredFieldException(String argument) {
    super();
    this.argument = argument;
  }

  @Override
  public String getMessage() {
    return String.format("missing field: %s", argument);
  }
}
