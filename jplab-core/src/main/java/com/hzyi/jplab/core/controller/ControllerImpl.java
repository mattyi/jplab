package com.hzyi.jplab.core.controller;

import java.util.HashMap;
import java.util.Collection;
import java.util.Map;

public class ControllerImpl implements Controller {

  Map<String, Parameter<?>> parameters = new HashMap<>();

  @Override
  public void addParameter(Parameter<?> parameter) {
    if (parameters.containsKey(parameter.getName())) {
      throw new IllegalArgumentException(
          "Parameter named " + parameter.getName() + " already exists.");
    }
    parameters.put(parameter.getName(), parameter);
  }

  @Override
  public Collection<Parameter<?>> getParameters() {
    return this.parameters.values();
  }

  @Override
  public Parameter getParameter(String name) {
    Parameter param = parameters.get(name);
    if (param == null) {
      throw new IllegalArgumentException("Parameter named " + name + " does not exist.");
    }
    return param;
  }

}