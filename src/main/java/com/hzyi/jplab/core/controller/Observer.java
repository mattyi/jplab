package com.hzyi.jplab.core.controller;

public interface Observer<E> {

  public void update(E value);
}
