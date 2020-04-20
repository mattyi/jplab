package com.hzyi.jplab.core.model.kinematic;

import com.hzyi.jplab.core.painter.CoordinateTransformer;
import com.hzyi.jplab.core.util.Coordinate;
import com.hzyi.jplab.core.util.Coordinates;
import java.util.HashMap;
import java.util.Map;

public abstract class ConnectingModel implements KinematicModel {

  public abstract SingleKinematicModel connectingModelA();

  public abstract Coordinate relativeConnectingPointA();

  public Coordinate absoluteConnectingPointA() {
    return Coordinates.transform(
        relativeConnectingPointA(),
        connectingModelA().bodyCoordinateSystem(),
        CoordinateTransformer.absoluteNatural());
  }

  public abstract SingleKinematicModel connectingModelB();

  public abstract Coordinate relativeConnectingPointB();

  public Coordinate absoluteConnectingPointB() {
    return Coordinates.transform(
        relativeConnectingPointB(),
        connectingModelB().bodyCoordinateSystem(),
        CoordinateTransformer.absoluteNatural());
  }

  public double length() {
    return Coordinates.distance(absoluteConnectingPointA(), absoluteConnectingPointB());
  }

  public final double theta() {
    return Math.atan2(
        absoluteConnectingPointB().y() - absoluteConnectingPointA().y(),
        absoluteConnectingPointB().x() - absoluteConnectingPointA().x());
  }

  @Override
  public Map<String, Object> pack() {
    Map<String, Object> answer = new HashMap<>();
    answer.put("connecting_model_a", connectingModelA());
    answer.put("connecting_model_b", connectingModelB());
    return answer;
  }
}
