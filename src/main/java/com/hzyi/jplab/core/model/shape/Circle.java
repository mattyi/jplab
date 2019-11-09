package com.hzyi.jplab.core.model.shape;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Builder(builderMethodName = "newBuilder")
public class Circle implements Shape {

  @Getter
  @Accessors(fluent = true)
  private double radius;

  @Getter private Appearance appearance;
}
