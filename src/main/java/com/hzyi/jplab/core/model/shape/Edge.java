package com.hzyi.jplab.core.model.shape;

import lombok.Builder;
import lombok.Getter;

@Builder(builderMethodName = "newBuilder")
public class Edge implements Shape {

  @Getter(fluent = true)
  private double length;

  @Getter(fluent = true)
  private double innerLineAngle;

  @Getter(fluent = true)
  private double innerLineCount;

  @Getter(fluent = true)
  private double innerLineHeight;

  @Getter private Appearance appearance;
}
