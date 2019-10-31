package com.hzyi.jplab.core.util;

import static com.google.common.base.Preconditions.checkArgument;

public class CoordinateSystem {
  /**
   * implicitly the absolute coordinate system. x positive direction: right y positive direction: up
   * unit length: 1 pixel on screen origin: center of the canvas
   */

  /** The natural coordinate system. x: (ratio, 0) y: (0, ratio) origin: Fixed at (0, 0) */
  private static final CoordinateSystem NATURAL = new CoordinateSystem(0, 0, 1, 0, 0, 1);

  /*
   * The screen coordinate system.
   * x: (1, 0) fixed
   * y: (0, 1) fixed
   * origin: the top-left corner of the canvas
   *
   */
  private static final CoordinateSystem SCREEN = new CoordinateSystem(0, 0, 1, 0, 0, -1);

  private Coordinate ux;
  private Coordinate uy;
  private Coordinate origin;

  public CoordinateSystem(Coordinate origin, Coordinate ux, Coordinate uy) {
    this.origin = origin;
    this.ux = ux;
    this.uy = uy;
    checkArgument(
        Coordinates.areOrthogonal(ux, uy),
        "Non-orthogonal coordinate systems are currently not supported");
  }

  public CoordinateSystem(double ox, double oy, double uxx, double uxy, double uyx, double uyy) {
    this.origin = new Coordinate(ox, oy);
    this.ux = new Coordinate(uxx, uxy);
    this.uy = new Coordinate(uyx, uyy);
    checkArgument(
        Coordinates.areOrthogonal(ux, uy),
        "Non-orthogonal coordinate systems are currently not supported");
  }

  public Coordinate ux() {
    return ux;
  }

  public Coordinate uy() {
    return uy;
  }

  public CoordinateSystem ux(double x, double y) {
    ux().x(x).y(y);
    return this;
  }

  public CoordinateSystem uy(double x, double y) {
    uy().x(x).y(y);
    return this;
  }

  public Coordinate origin() {
    return origin;
  }

  public CoordinateSystem origin(double x, double y) {
    origin().x(x).y(y);
    return this;
  }

  public String toString() {
    return String.format("{o: %s, ux: %s, uy: %s}", origin(), ux(), uy());
  }

  public static CoordinateSystem natural(double ratio) {
    NATURAL.ux().x(ratio);
    NATURAL.uy().y(ratio);
    return NATURAL;
  }

  public static CoordinateSystem screen(double width, double height) {
    SCREEN.origin.x(-width / 2).y(height / 2);
    return SCREEN;
  }
}
