package com.hzyi.jplab.core.model.shape;

import com.hzyi.jplab.core.util.UnpackHelper;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Builder(builderMethodName = "newBuilder")
public class Circle implements Shape {

  @Getter
  @Accessors(fluent = true)
  private double radius;

  @Getter private Appearance appearance;

  public Shape.Type type() {
    return Shape.Type.CIRCLE;
  }

  public static Circle unpack(Map<String, ?> map) {
    CircleBuilder builder = newBuilder();
    UnpackHelper<CircleBuilder> helper = UnpackHelper.of(builder, map, Circle.class);
    helper.unpack(
        "radius",
        Double.class,
        CircleBuilder::radius,
        UnpackHelper.checkExistence(),
        UnpackHelper.checkPositivity());
    return helper.getBuilder().build();
  }
}
