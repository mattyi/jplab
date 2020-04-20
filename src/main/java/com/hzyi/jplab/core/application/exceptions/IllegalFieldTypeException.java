package com.hzyi.jplab.core.application.exceptions;

public class IllegalFieldTypeException extends IllegalArgumentException {

  private final String argument;
  private final String argumentType;
  private final String expectedType;

  public IllegalFieldTypeException(String argument, String argumentType, String expectedType) {
    super();
    this.argument = argument;
    this.argumentType = argumentType;
    this.expectedType = expectedType;
  }

  @Override
  public String getMessage() {
    return String.format("field %s has type %s, but want %s", argument, argumentType, expectedType);
  }
}
