package com.hzyi.jplab.core.model.kinematic;

import com.hzyi.jplab.core.painter.CoordinateTransformer;
import com.hzyi.jplab.core.util.Coordinate;
import com.hzyi.jplab.core.util.Coordinates;

public abstract class ConnectingModel implements KinematicModel {

  public abstract KinematicModel connectingModelA();

  public abstract Coordinate relativeConnectingPointA();

  public Coordinate absoluteConnectingPointA() {
    return Coordinates.transform(
        relativeConnectingPointA(),
        connectingModelA().bodyCoordinateSystem(),
        CoordinateTransformer.absoluteNatural());
  }

  public abstract KinematicModel connectingModelB();

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

  @Override
  public final double x() {
    return (absoluteConnectingPointA().x() + absoluteConnectingPointB().x()) / 2;
  }

  @Override
  public final double y() {
    return (absoluteConnectingPointA().y() + absoluteConnectingPointB().y()) / 2;
  }

  @Override
  public final double theta() {
    return Math.atan2(
        absoluteConnectingPointB().y() - absoluteConnectingPointA().y(),
        absoluteConnectingPointB().x() - absoluteConnectingPointA().x());
  }

  @Override
  public final double vx() {
    return (connectingModelA().vx() + connectingModelB().vx()) / 2;
  }

  @Override
  public final double vy() {
    return (connectingModelA().vy() + connectingModelB().vy()) / 2;
  }

  @Override
  public final double omega() {
    return (connectingModelB().vy() - connectingModelA().vy())
        / (connectingModelB().vx() - connectingModelA().vx());
  }

  @Override
  public final double ax() {
    return (connectingModelA().ax() + connectingModelB().ax()) / 2;
  }

  @Override
  public final double ay() {
    return (connectingModelA().ay() + connectingModelB().ay()) / 2;
  }

  @Override
  public final double alpha() {
    return (connectingModelB().ay() - connectingModelA().ay())
        / (connectingModelB().ax() - connectingModelA().ax());
  }
}
