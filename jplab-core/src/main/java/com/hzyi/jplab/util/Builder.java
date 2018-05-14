package com.hzyi.jplab.util;

public interface Builder<T extends Buildable> {
  T build();
  Builder<T> mergeFrom(Builder<T> builder);
}
