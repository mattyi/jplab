package com.hzyi.jplab.core.controller;

public interface Parameter<E> {
  void addObserver(Observer<E> observer);

  String getName();

  void setValue(E value);

  E getValue();

  public static Parameter<Boolean> newBooleanParameter(boolean initValue, String name) {
    return new ParameterImpl(initValue, name);
  }

  public static Parameter<String> newStringParameter(String initValue, String name) {
    return new ParameterImpl(initValue, name);
  }

  public static Parameter<Double> newDoubleParameter(double initValue, String name) {
    return new ParameterImpl(initValue, name);
  }

  public static Parameter<Integer> newIntegerParameter(int initValue, String name) {
    return new ParameterImpl(initValue, name);
  }
}
