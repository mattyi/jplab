package com.hzyi.jplab.core.application.exceptions;

public class ResourceExhaustedException extends IllegalArgumentException {

  @Override
  public String getMessage() {
    return String.format("Calculation resources exhausted. Please use a larger time step.");
  }
}
