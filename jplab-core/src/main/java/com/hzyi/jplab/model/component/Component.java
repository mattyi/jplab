package com.hzyi.jplab.model.component;

import com.hzyi.jplab.util.Buildable;
import com.hzyi.jplab.util.Builder;

public abstract class Component implements Buildable {

  private final String name;

  public String getName() {
    return name;
  }

  @Override
  public Builder<Component> newBuilder() {
    return new Builder();
  }

  @Override
  public Builder<Component> toBuilder() {

  }


  public abstract class Builder<T extends Component> implements Builder<Component> {
    
    private String name;

    public Builder<T> setName(String name) {
      this.name = name;
    }

    @Override
    public Builder<T> mergeFrom(Builder<T> builder) {
      this.name = builder.name;
    }

    @Override
    public T build() {
      throw UnsupportedOperationException("build() is not supported Component");
    }
  }
  
}