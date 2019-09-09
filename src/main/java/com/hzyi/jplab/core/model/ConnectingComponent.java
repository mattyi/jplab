package com.hzyi.jplab.core.model;

public abstract class ConnectingComponent implements Component {

  public abstract Component componentA();

  // relative to A
  public abstract double connectingPointAX();

  // relative to A
  public abstract double connectingPointAY();

  public abstract Component componentB();

  // relative to B
  public abstract double connectingPointBX();

  // relative to B
  public abstract double connectingPointBY();

  @Override
  public double x() {
    return (componentA().x() + componentB().x()) / 2;
  }

  @Override
  public double y() {
    return (componentA().y() + componentB().y()) / 2;
  }

  @Override
  public double theta() {
    return (componentB().y() - componentA().y()) / (componentB().x() - componentA().x()); 
  }

  @Override
  public double vx() {
    return (componentA().vx() + componentB().vx()) / 2;
  }

  @Override
  public double vy() {
    return (componentA().vy() + componentB().vy()) / 2;
  }

  @Override
  public double omega() {
    return (componentB().vy() - componentA().vy()) / (componentB().vx() - componentA().vx());
  }

  @Override
  public double ax() {
    return (componentA().ax() + componentB().ax()) / 2;
  }

  @Override
  public double ay() {
    return (componentA().ay() + componentB().ay()) / 2;
  }

  @Override
  public double alpha() {
    return (componentB().ay() - componentA().ay()) / (componentB().ax() - componentA().ax());
  }
}