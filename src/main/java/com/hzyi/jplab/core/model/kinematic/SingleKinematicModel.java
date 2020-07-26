package com.hzyi.jplab.core.model.kinematic;

import com.hzyi.jplab.core.application.Application;
import com.hzyi.jplab.core.model.Component;
import com.hzyi.jplab.core.model.shape.Paintable;
import com.hzyi.jplab.core.util.CoordinateSystem;
import java.util.HashMap;
import java.util.Map;

/** A SingleKinematicModel is a single kinematic model that can move or rotate. */
public abstract class SingleKinematicModel
    implements Paintable, Component, PropertyProvider, MultiplierProvider, ConstraintProvider {

  public abstract SingleKinematicModel.Type type();

  public abstract double x();

  public abstract double y();

  public abstract double theta();

  public abstract double vx();

  public abstract double vy();

  public abstract double omega();

  public abstract double ax();

  public abstract double ay();

  public abstract double alpha();

  public CoordinateSystem bodyCoordinateSystem() {
    return Application.getCoordinateTransformer().getBodyCoordinateSystem(x(), y());
  }

  @Override
  public Map<String, Object> pack() {
    Map<String, Object> answer = new HashMap<>();
    answer.put("x", x());
    answer.put("y", y());
    answer.put("theta", theta());
    answer.put("vx", vx());
    answer.put("vy", vy());
    answer.put("omega", omega());
    answer.put("ax", ax());
    answer.put("ay", ay());
    answer.put("alpha", alpha());
    answer.put("name", name());
    return answer;
  }

  @Override
  public abstract SingleKinematicModel merge(Map<String, ?> map);

  public boolean isRigidBody() {
    return this instanceof RigidBody;
  }

  public static enum Type {
    MASS_POINT,
    STATIC_MODEL
  }
}
