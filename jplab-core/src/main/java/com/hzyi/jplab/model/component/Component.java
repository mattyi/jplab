package com.hzyi.jplab.model.component;

import com.hzyi.jplab.util.Buildable;

public class Component implements Buildable {

  protected final String name;

  Component(Builder<?> builder) {
    this.name = builder.name;
  }

  public String getName() {
    return name;
  }

  public static abstract class Builder<T extends Builder<T>> 
      implements com.hzyi.jplab.util.Builder<T> {
    
    private String name;

    @SuppressWarnings("Unchecked")
    public T setName(String name) {
      this.name = name;
      return (T)this;
    }

    @SuppressWarnings("Unchecked")
    @Override
    public T mergeFrom(T builder) {
      if (builder instanceof Builder<?>) {
        Builder<T> b = (Builder<T>)builder;
        this.name = b.name;
        return (T)this;
      }
      throw new IllegalArgumentException("builder must be instance of Component.Builder");
    }

    @Override
    public Component build() {
      throw new UnsupportedOperationException("build() is not supported Component");
    }
  }
  
}