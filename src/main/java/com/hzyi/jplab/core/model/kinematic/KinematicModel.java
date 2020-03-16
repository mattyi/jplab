package com.hzyi.jplab.core.model.kinematic;

import com.hzyi.jplab.core.util.Coordinate;
import com.hzyi.jplab.core.util.CoordinateSystem;
import java.util.Map;

public interface KinematicModel {

  String name();

  Coordinate getLocation();

  CoordinateSystem bodyCoordinateSystem();

  Map<String, Object> pack();

  KinematicModel unpack(Map<String, Object> map);
}
