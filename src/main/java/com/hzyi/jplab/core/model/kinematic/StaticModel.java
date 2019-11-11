package com.hzyi.jplab.core.model.kinematic;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Builder(builderMethodName = "newBuilder", toBuilder = true)
public class StaticModel implements KinematicModel {

  @Getter private final double x;
  @Getter private final double y;
  @Getter private final double theta;

  @Override
  public final double vx() {
    return 0;
  }

  @Override
  public final double vy() {
    return 0;
  }

  @Override
  public final double omega() {
    return 0;
  }

  @Override
  public final double ax() {
    return 0;
  }

  @Override
  public final double ay() {
    return 0;
  }

  @Override
  public final double alpha() {
    return 0;
  }
}
