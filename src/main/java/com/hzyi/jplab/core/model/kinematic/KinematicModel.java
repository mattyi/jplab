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

  List<String> codependentProperties();

  Table<String, String, Double> codependentMultipliers(double timeStep);

  public default boolean isConnector() {
    return type().isConnector();
  }

  public default boolean isSingleModel() {
    return type().isSingleModel();
  }

  public default boolean isRigidBody() {
    return this instanceof RigidBody;
  }

  public enum Type {
    MASS_POINT,
    STATIC_MODEL,
    SPRING_MODEL,
    ROPE_MODEL;

    public boolean isConnector() {
      return this == SPRING_MODEL || this == ROPE_MODEL;
    }

    public boolean isSingleModel() {
      return this == MASS_POINT || this == STATIC_MODEL;
    }
  }
}
