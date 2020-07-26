package com.hzyi.jplab.core.model;

import com.hzyi.jplab.core.model.kinematic.Connector;
import com.hzyi.jplab.core.model.kinematic.ConstraintProvider;
import com.hzyi.jplab.core.model.kinematic.Field;
import com.hzyi.jplab.core.model.kinematic.MultiplierProvider;
import com.hzyi.jplab.core.model.kinematic.PropertyProvider;
import com.hzyi.jplab.core.model.kinematic.SingleKinematicModel;
import com.hzyi.jplab.core.model.kinematic.VerifierProvider;
import com.hzyi.jplab.core.model.shape.Paintable;

public class Components {

  // -- Identity

  public static boolean isField(Component component) {
    return component instanceof Field;
  }

  public static boolean isKinematicModel(Component component) {
    return component instanceof SingleKinematicModel;
  }

  public static boolean isConnector(Component component) {
    return component instanceof Connector;
  }

  // -- Mathematics

  public static boolean hasMultipliers(Component component) {
    return component instanceof MultiplierProvider;
  }

  public static boolean hasVerifiers(Component component) {
    return component instanceof VerifierProvider;
  }

  public static boolean hasConstraints(Component component) {
    return component instanceof ConstraintProvider;
  }

  public static boolean hasProperties(Component component) {
    return component instanceof PropertyProvider;
  }

  // -- Visualization

  public static boolean isPaintable(Component component) {
    return component instanceof Paintable;
  }
}
