package com.hzyi.jplab.core.model.kinematic;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Builder(builderMethodName = "newBuilder", toBuilder = true)
public class MassPoint extends RigidBody {

  @Getter private String name;
  @Getter private double x;
  @Getter private double y;
  @Getter private double vx;
  @Getter private double vy;
  @Getter private double ax;
  @Getter private double ay;
  @Getter private double mass;

  @Override
  public final double theta() {
    return 0;
  }

  @Override
  public final double omega() {
    return 0;
  }

  @Override
  public final double alpha() {
    return 0;
  }

  @Override
  public final double momentOfInertia() {
    return Double.POSITIVE_INFINITY;
  }

  @Override
  public MassPoint unpack(Map<String, Object> map) {
    return toBuilder()
        .x((Double) map.get("x"))
        .y((Double) map.get("y"))
        .vx((Double) map.get("vx"))
        .vy((Double) map.get("vy"))
        .ax((Double) map.get("ax"))
        .ay((Double) map.get("ay"))
        .build();
  }
}
