package com.hzyi.jplab.util;

public interface Buildable {

  Builder<Buildable> newBuilder();
  Builder<Buildable> toBuilder();
}