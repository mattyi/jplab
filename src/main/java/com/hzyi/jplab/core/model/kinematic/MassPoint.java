package com.hzyi.jplab.core.model.kinematic;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessor;

@Accessor(fluent = true)
@Builder(builderMethodName = "newBuilder")
public class MassPoint implements RigidBody {

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
    return 0;
  }
}
