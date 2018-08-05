package com.hzyi.jplab.core.controller;

import java.util.Collection;

public interface Controller {

  public void addParameter(Parameter<?> parameter);

  public Collection<Parameter<?>> getParameters();

  public Parameter<?> getParameter(String name);

  public static Controller newController() {
    return new ControllerImpl();
  }

}