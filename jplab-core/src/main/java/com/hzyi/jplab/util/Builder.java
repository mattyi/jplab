package com.hzyi.jplab.util;

public interface Builder<T extends Builder<T>> {
  Buildable build();
  T mergeFrom(Builder<T> builder);
}
