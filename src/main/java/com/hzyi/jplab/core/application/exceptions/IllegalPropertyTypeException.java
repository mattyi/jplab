package com.hzyi.jplab.core.application.exceptions;

public class IllegalPropertyTypeException extends IllegalArgumentException {

  private final String argument;
  private final String argumentType;
  private final String expectedType;

  public IllegalPropertyTypeException(String argument, String argumentType, String expectedType) {
    super();
    this.argument = argument;
    this.argumentType = argumentType;
    this.expectedType = expectedType;
  }

  @Override
  public String getMessage() {
    return String.format(
        "property %s has type %s, but want %s", argument, argumentType, expectedType);
  }
}
