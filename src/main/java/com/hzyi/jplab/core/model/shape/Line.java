package com.hzyi.jplab.core.model.shape;

import lombok.Getter;

public class Line implements Shape {

  @Getter(fluent = true)
  private double length;

  @Getter(fluent = true)
  private double width;

  @Getter private Appearance appearance;
}
