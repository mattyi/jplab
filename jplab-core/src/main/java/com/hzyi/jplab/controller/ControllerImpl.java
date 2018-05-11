package com.hzyi.jplab.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ControllerImpl implements Controller {

  Map<String, Parameter<?>> parameters = new HashMap<>();

  @Override
  public void addParameter(Parameter<?> parameter) {
    if (parameters.containsKey(parameter.getName())) {
      throw new IllegalArgumentException(
          "parameter named " + parameter.getName() + "already exists.");
    }
  }

  @Override
  public Iterator<Parameter<?>> getParameters() {
    return this.parameters.values().iterator();
  }

  @Override
  public Parameter getParameter(String name) {
    return this.parameters.get(name);
  }

}