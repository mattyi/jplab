package com.hzyi.jplab.core.model.kinematic;

import com.hzyi.jplab.core.util.Coordinate;
import com.hzyi.jplab.core.util.CoordinateSystem;

public interface KinematicModel {

  String name();

  double x();

  double y();

  double theta();

  double vx();

  double vy();

  double omega();

  double ax();

  double ay();

  double alpha();

  default Coordinate getLocation() {
    return new Coordinate(x(), y());
  }

  default CoordinateSystem bodyCoordinateSystem() {
    return new CoordinateSystem(x(), y(), 1, 0, 0, 1);
  }
}
