package com.hzyi.jplab.core.model.shape;

import java.util.Map;
import lombok.Builder;

@Builder(builderMethodName = "newBuilder")
public class Catenary implements Shape {

  private static final Catenary INSTANCE = newBuilder().build();

  public Shape.Type type() {
    return Shape.Type.CATENARY;
  }

  public static final Catenary unpack(Map<String, ?> map) {
    return INSTANCE;
  }
}
