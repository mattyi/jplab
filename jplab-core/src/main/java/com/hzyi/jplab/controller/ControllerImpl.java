package com.hzyi.jplab.controller;

public class ControllerImpl implements Controller {

  private static final Set<Class> SUPPORTED_CLASSES = 
      ImmutableSet.of(String.class, Boolean.class, Double.class, Integer.class);
  
  private final Map<Class, List<Parameter>> parameters;

  public <E> void addParameter(Parameter<E> parameter) {
    List<Parameter> list = parameters.get(E.class);
    if (list != null) {
      list.add(parameter);
    }
  }

}