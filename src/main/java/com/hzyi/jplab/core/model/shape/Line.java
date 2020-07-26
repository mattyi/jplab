package com.hzyi.jplab.core.model.shape;

import java.util.Map;
import lombok.Builder;
import lombok.ToString;

@ToString
@Builder(builderMethodName = "newBuilder")
public class Line implements Shape {

  private static final Line INSTANCE = newBuilder().build();

  public Shape.Type type() {
    return Shape.Type.LINE;
  }

  public static final Line of(Map<String, ?> map) {
    return INSTANCE;
  }
}
