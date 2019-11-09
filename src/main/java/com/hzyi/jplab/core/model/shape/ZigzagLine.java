package com.hzyi.jplab.core.model.shape;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Builder(builderMethodName = "newBuilder")
public class ZigzagLine implements Shape {

  @Getter
  @Accessors(fluent = true)
  private double length;

  @Getter
  @Accessors(fluent = true)
  private double width;

  @Getter
  @Accessors(fluent = true)
  private int zigzagCount;

  @Getter private Appearance appearance;
}
