package com.hzyi.jplab.core.model.kinematic;

import com.hzyi.jplab.core.util.Coordinate;
import com.hzyi.jplab.core.util.Coordinates;

public abstract class ConnectingModel implements KinematicModel {

  public abstract KinematicModel connectingModelA();

  public abstract double connectingPointAX();

  public abstract double connectingPointAY();

  public abstract KinematicModel connectingModelB();

  public abstract double connectingPointBX();

  public abstract double connectingPointBY();

  public Coordinate connectingPointA() {
    return new Coordinate(connectingPointAX(), connectingPointAY());
  }

  public Coordinate connectingPointB() {
    return new Coordinate(connectingPointBX(), connectingPointBY());
  }

  public double length() {
    return Coordinates.distance(connectingPointA(), connectingPointB());
  }

  @Override
  public final double x() {
    return (connectingPointAX() + connectingPointBX()) / 2;
  }

  @Override
  public final double y() {
    return (connectingPointAY() + connectingPointBY()) / 2;
  }

  @Override
  public final double theta() {
    return Math.atan2(
        connectingPointBY() - connectingPointAY(), connectingPointBX() - connectingPointAX());
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
