package com.hzyi.jplab.controller;

import java.util.ArrayList;
import java.util.List;

public class ParameterImpl<E> implements Parameter<E> {

  private final List<Observer<E>> observers;
  private E value;
  private final String name;

  ParameterImpl(E initValue, String name) {
    this.value = initValue;
    this.name = name;
    observers = new ArrayList<>();
  }

  @Override
  public void addObserver(Observer<E> observer) {
    this.observers.add(observer);
  }

  @Override
  public void setValue(E e) {
    this.value = e;
    for (Observer<E> observer : observers) {
      observer.update(e);
    }
  }

  @Override
  public E getValue() {
    return this.value;
  }

  @Override
  public String getName() {
    return this.name;
  }

}