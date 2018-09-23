package com.hzyi.jplab.core.model;

import com.hzyi.jplab.core.util.Buildable;
import com.hzyi.jplab.core.viewer.Painter;
import com.hzyi.jplab.core.viewer.DisplayContext;

public class Component implements Buildable {

  protected final String name;
  protected ComponentState initState;
  private final Painter painter;
  private final DisplayContext context;

  Component(Builder<?> builder) {
    this.name = builder.name;
    this.initState = newComponentStateBuilder(builder).build();
    this.painter = builder.painter;
    this.context = builder.context;
  }

  public ComponentState getInitialComponentState() {
    return null;
  }

  public String getName() {
    return name;
  }

  public Painter getPainter() {
    return painter;
  }

  public DisplayContext getDisplayContext() {
    return context;
  }

  protected ComponentState.Builder newComponentStateBuilder(Builder<?> builder) {
    return ComponentState.newBuilder()
        .setComponent(this);
  }

  public static abstract class Builder<T extends Builder<T>> 
      implements com.hzyi.jplab.core.util.Builder<T> {
    
    private String name;
    private Painter painter;
    private DisplayContext context;

    @SuppressWarnings("Unchecked")
    public T setName(String name) {
      this.name = name;
      return (T)this;
    }

    @SuppressWarnings("Unchecked")
    public T setPainter(Painter painter) {
      this.painter = painter;
      return (T)this;
    }

    @SuppressWarnings("Unchecked")
    public T setDisplayContext(DisplayContext context) {
      this.context = context;
      return (T)this;
    }

    @Override
    public Component build() {
      throw new UnsupportedOperationException("build() is not supported for Component");
    }
  }
  
}