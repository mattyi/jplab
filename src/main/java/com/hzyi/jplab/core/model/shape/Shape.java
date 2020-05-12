package com.hzyi.jplab.core.model.shape;

public interface Shape {

  public Type type();

  public static enum Type {
    CIRCLE,
    EDGE,
    LINE,
    ZIGZAG_LINE,
    CATENARY
  }
}
