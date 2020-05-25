package com.hzyi.jplab.core.model.kinematic;

import com.hzyi.jplab.core.model.Constraint;
import com.hzyi.jplab.core.model.Property;
import com.hzyi.jplab.core.util.CoordinateSystem;
import java.util.List;
import java.util.Map;

public interface KinematicModel extends MultiplierProvider {

  String name();

  Type type();

  CoordinateSystem bodyCoordinateSystem();

  Map<String, Object> pack();

  KinematicModel merge(Map<String, ?> map);

  List<Constraint> constraints();

  List<Property> properties();

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
    ROD_MODEL,
    ROPE_MODEL;

    public boolean isConnector() {
      return this == SPRING_MODEL || this == ROPE_MODEL || this == ROD_MODEL;
    }

    public boolean isSingleModel() {
      return this == MASS_POINT || this == STATIC_MODEL;
    }
  }
}
