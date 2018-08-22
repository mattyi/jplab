package com.hzyi.jplab.core.util;

import static com.google.common.base.Preconditions.checkArgument;

public class CoordinateSystem {

  private static final CoordinateSystem NATURAL = new CoordinateSystem(0, 0, 1, 0, 0, 1);
  private static final CoordinateSystem SCREEN = new CoordinateSystem(0, 0, 1, 0, 0, -1);

  // the unit vector of x and y axes if in natural coordinate system
  private Coordinate ux;
  private Coordinate uy;
  private Coordinate origin;

  private CoordinateSystem(Coordinate origin, Coordinate ux, Coordinate uy) {
    this.origin = origin;
    this.ux = ux;
    this.uy = uy;
    checkArgument(
        Coordinates.areOrthogonal(ux, uy),
        "Non-orthogonal coordinate systems are currently not supported");
  }

  private CoordinateSystem(double ox, double oy, double uxx, double uxy, double uyx, double uyy) {
    this.origin = new Coordinate(ox, oy);
    this.ux = new Coordinate(uxx, uxy);
    this.uy = new Coordinate(uyx, uyy);
  }

  Coordinate ux() {
    return ux;
  }

  Coordinate uy() {
    return uy;
  }

  Coordinate origin() {
    return origin;
  }

  public static CoordinateSystem natural() {
    return NATURAL;
  }

  public static CoordinateSystem screen(double width, double height) {
    SCREEN.origin.x(-width / 2).y(height / 2);
    return SCREEN;
  }
}
