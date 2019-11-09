package com.hzyi.jplab.core.model.kinematic;

import com.hzyi.jplab.core.util.Coordinate;

public interface KinematicModel {

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
}
