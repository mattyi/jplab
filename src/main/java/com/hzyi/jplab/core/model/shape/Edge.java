package com.hzyi.jplab.core.model.shape;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Builder(builderMethodName = "newBuilder")
public class Edge implements Shape {

  @Getter
  @Accessors(fluent = true)
  private double length;

  @Getter
  @Accessors(fluent = true)
  private double innerLineAngle;

  @Getter
  @Accessors(fluent = true)
  private double innerLineCount;

  @Getter
  @Accessors(fluent = true)
  private double innerLineHeight;

  @Getter private Appearance appearance;
}
