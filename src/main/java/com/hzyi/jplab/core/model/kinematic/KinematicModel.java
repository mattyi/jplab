package com.hzyi.jplab.core.model.kinematic;

import com.google.common.collect.Table;
import com.hzyi.jplab.core.util.CoordinateSystem;
import java.util.List;
import java.util.Map;

public interface KinematicModel {

  String name();

  Type type();

  CoordinateSystem bodyCoordinateSystem();

  Map<String, Object> pack();

  KinematicModel merge(Map<String, ?> map);

  List<String> codependentPropertys();

  Table<String, String, Double> codependentMultipliers(double timeStep);

  public default isConnectingModel() {
    return getType().isConnectingModel();
  }

  public default isSingleModel() {
    return getType().isSingleModel();
  }

  public default isRigidBody() {
    
  }

  public enum Type {
    MASS_POINT,
    STATIC_MODEL,
    SPRING_MODEL,
    ROPE_MODEL;

    public boolean isConnectingModel() {
      return this == SPRING_MODEL || this == ROPE_MODEL;
    }

    public boolean isSingleModel() {
      return this == MASS_POINT || this == STATIC_MODEL;
    }
  }
}
