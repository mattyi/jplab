package com.hzyi.jplab.controller;

public interface Parameter<E> {
  public void addObserver(Observer observer);

  public void setValue(E value);

  public E getValue();

  public static Parameter<Boolean> newBooleanParameter(boolean initValue) {
    return new Parameter(initValue);
  }

  public static Parameter<String> newStringParameter(String initValue) {
    return new Parameter(initValue);
  }

  public static Parameter<Double> newDoubleParameter(double initValue) {
    return new Parameter(initValue);
  }

  public static Parameter<Integer> newIntegerParameter(int initValue) {
    return new Parameter(initValue);
  }
}