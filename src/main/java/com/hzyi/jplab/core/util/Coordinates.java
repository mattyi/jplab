package com.hzyi.jplab.core.util;

public class Coordinates {

  private static final double EPSILON = 0.00000001;

  private Coordinates() {
    // utility class
  }

  public static boolean areOrthogonal(Coordinate c1, Coordinate c2) {
    return areEqual(c1.x() * c2.x() + c1.y() * c2.y(), 0, EPSILON);
  }

  public static boolean areEqual(Coordinate c1, Coordinate c2) {
    return areEqual(c1.x(), c2.x(), EPSILON) && areEqual(c1.y(), c2.y(), EPSILON);
  }

  public static double distance(Coordinate c1, Coordinate c2) {
    return Math.sqrt((c1.x() - c2.x()) * (c1.x() - c2.x()) + (c1.y() - c2.y()) * (c1.y() - c2.y()));
  }

  /**
   * @returns the {@code Coordinate} in {@code to} which is the same value as {@code coordinate} in
   *     {@code from}.
   */
  public static Coordinate transform(
      Coordinate coordinate, CoordinateSystem from, CoordinateSystem to) {
    double cnx =
        coordinate.x() * from.ux().x()
            + coordinate.y() * from.uy().x()
            + from.origin().x()
            - to.origin().x();
    double cny =
        coordinate.y() * from.uy().y()
            + coordinate.x() * from.ux().y()
            + from.origin().y()
            - to.origin().y();
    double ctx =
        (cnx * to.uy().y() - cny * to.uy().x())
            / (to.ux().x() * to.uy().y() - to.ux().y() * to.uy().x());
    double cty =
        (cnx * to.ux().y() - cny * to.ux().x())
            / (to.uy().x() * to.ux().y() - to.uy().y() * to.ux().x());
    return new Coordinate(ctx, cty);
  }

  public static void transform(
      Coordinate coordinateFrom,
      Coordinate coordinateTo,
      CoordinateSystem from,
      CoordinateSystem to) {
    double cnx = coordinateFrom.x() * from.ux().x() + from.origin().x();
    double cny = coordinateFrom.y() * from.uy().y() + from.origin().y();
    double ctx =
        (cnx * to.uy().y() - cny * to.uy().x())
                / (to.ux().x() * to.uy().y() - to.ux().y() * to.uy().x())
            - to.origin().x();
    double cty =
        (cnx * to.ux().y() - cny * to.ux().x())
                / (to.uy().x() * to.ux().y() - to.uy().y() * to.ux().x())
            - to.origin().y();
    coordinateTo.x(ctx);
    coordinateTo.y(cty);
  }

  private static final boolean areEqual(double v1, double v2, double epsilon) {
    return v1 > v2 ? v1 - v2 <= epsilon : v2 - v1 <= epsilon;
  }
}
