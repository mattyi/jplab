package com.hzyi.jplab.core.model.kinematic;

import com.hzyi.jplab.core.util.CoordinateSystem;
import java.util.HashMap;
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
  public CoordinateSystem bodyCoordinateSystem() {
    return new CoordinateSystem(x(), y(), 1, 0, 0, 1);
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
}
