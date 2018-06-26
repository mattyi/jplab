package com.hzyi.jplab.core.util;

public interface Builder<T extends Builder<T>> {
  Buildable build();
  T mergeFrom(T builder);
}
