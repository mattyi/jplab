package com.hzyi.jplab.controller;

import java.util.ArrayList;

public ParameterImpl<E> implements Parameter<E> {

  private List<Observer<E>> observers;
  private E value;

  ParameterImpl(E initValue) {
    this.value = initValue;
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

}