package com.hzyi.jplab.core.util;

/** A two dimensional coordinate. */
public class Coordinate {

  private double x, y;
  
  public Coordinate(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public double x() {
    return x;
  }

  public double y() {
    return y;
  }

  public Coordinate x(double x) {
    this.x = x;
    return this;
  }

  public Coordinate y(double y) {
    this.y = y;
    return this;
  }

  public double dot(Coordinate c2) {
  	return x * c2.x + y * c2.y;
  }

  @Override
  public String toString() {
    return String.format("[%f, %f]", x, y);
  }
}