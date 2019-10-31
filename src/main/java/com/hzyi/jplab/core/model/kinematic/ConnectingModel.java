package com.hzyi.jplab.core.model.kinematic;

import com.hzyi.jplab.core.model.Component;
import com.hzyi.jplab.core.util.Coordinate;
import com.hzyi.jplab.core.util.Coordinates;

public abstract class ConnectingModel implements KinematicModel {

  public abstract Component componentA();

  public abstract double connectingPointAX();

  public abstract double connectingPointAY();

  public abstract Component componentB();

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
    return (componentA().vx() + componentB().vx()) / 2;
  }

  @Override
  public final double vy() {
    return (componentA().vy() + componentB().vy()) / 2;
  }

  @Override
  public final double omega() {
    return (componentB().vy() - componentA().vy()) / (componentB().vx() - componentA().vx());
  }

  @Override
  public final double ax() {
    return (componentA().ax() + componentB().ax()) / 2;
  }

  @Override
  public final double ay() {
    return (componentA().ay() + componentB().ay()) / 2;
  }

  @Override
  public final double alpha() {
    return (componentB().ay() - componentA().ay()) / (componentB().ax() - componentA().ax());
  }
}
