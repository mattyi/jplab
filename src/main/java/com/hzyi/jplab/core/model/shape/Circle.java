package com.hzyi.jplab.core.model.shape;

import lombok.Builder;
import lombok.Getter;

@Builder(builderMethodName = "newBuilder")
public class Circle implements Shape {

  @Getter(fluent = true)
  private double radius;

  @Getter private Appearance appearance;
}
