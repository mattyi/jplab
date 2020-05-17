package com.hzyi.jplab.core.model.shape;

import java.util.Map;
import lombok.Builder;

@Builder(builderMethodName = "newBuilder")
public class Line implements Shape {

  private static final Line INSTANCE = newBuilder().build();

  public Shape.Type type() {
    return Shape.Type.LINE;
  }

  public static final Line unpack(Map<String, ?> map) {
    return INSTANCE;
  }
}
