package com.hzyi.jplab.controller;

import java.util.Iterator;

public interface Controller {

  public void addParameter(Parameter<?> parameter);

  public Iterator<Parameter<?>> getParameters();

  public Parameter<?> getParameter(String name);

}