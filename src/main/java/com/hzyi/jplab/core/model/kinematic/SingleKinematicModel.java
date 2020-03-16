package com.hzyi.jplab.core.model.kinematic;

import com.google.common.collect.ImmutableMap;
import com.hzyi.jplab.core.util.Coordinate;
import com.hzyi.jplab.core.util.CoordinateSystem;
import java.util.Map;

public abstract class SingleKinematicModel implements KinematicModel {

  public abstract double x();

  public abstract double y();

  public abstract double theta();

  public abstract double vx();

  public abstract double vy();

  public abstract double omega();

  public abstract double ax();

  public abstract double ay();

  public abstract double alpha();

  @Override
  public Coordinate getLocation() {
    return new Coordinate(x(), y());
  }

  @Override
  public CoordinateSystem bodyCoordinateSystem() {
    return new CoordinateSystem(x(), y(), 1, 0, 0, 1);
  }

  @Override
  public Map<String, Object> pack() {
    return ImmutableMap.<String, Object>builder()
        .put("x", x())
        .put("y", y())
        .put("theta", theta())
        .put("vx", vx())
        .put("vy", vy())
        .put("omega", omega())
        .put("ax", ax())
        .put("ay", ay())
        .put("alpha", alpha())
        .build();
  }
}
