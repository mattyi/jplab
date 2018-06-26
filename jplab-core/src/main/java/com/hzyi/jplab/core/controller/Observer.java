package com.hzyi.jplab.controller;

public interface Observer<E> {

  public void update(E value);
}