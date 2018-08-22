package com.hzyi.jplab.core.util;

public class Coordinates {

  private static final double EPSILON = 0.00000001;

  private Coordinates() {
  	// utility class
  }

  public static Coordinate prod(Coordinate c, double v) {
  	return new Coordinate(c.x() * v, c.y() * v);
  }

  public static Coordinate add(Coordinate c1, Coordinate c2) {
  	return new Coordinate(c1.x() + c2.x(), c1.y() + c2.y());
  }

  public static Coordinate prodAdd(Coordinate c1, double v1, Coordinate c2, double v2) {
  	return new Coordinate(c1.x() * v1 + c2.x() * v2, c1.y() * v1 + c2.y() * v2);
  }

  public static boolean areOrthogonal(Coordinate c1, Coordinate c2) {
    return areEqual(c1.x() * c2.x() + c1.y() * c2.y(), 0, EPSILON);
  }

  public Coordinate transform(Coordinate coordinate, CoordinateSystem from, CoordinateSystem to) {
    double cnx = coordinate.x() * from.ux().x() + from.origin().x();
    double cny = coordinate.y() * from.uy().y() + from.origin().y();
    double ctx = (cnx * to.uy().y() - cny * to.uy().x()) / (to.ux().x() * to.uy().y() - to.ux().y() * to.uy().x()) - to.origin().x();
    double cty = (cnx * to.ux().y() - cny * to.ux().x()) / (to.uy().x() * to.ux().y() - to.uy().y() * to.ux().x()) - to.origin().y();
    return new Coordinate(ctx, cty);
  }

  public void transformInPlace(Coordinate coordinate, CoordinateSystem from, CoordinateSystem to) {
    double cnx = coordinate.x() * from.ux().x() + from.origin().x();
    double cny = coordinate.y() * from.uy().y() + from.origin().y();
    double ctx = (cnx * to.uy().y() - cny * to.uy().x()) / (to.ux().x() * to.uy().y() - to.ux().y() * to.uy().x()) - to.origin().x();
    double cty = (cnx * to.ux().y() - cny * to.ux().x()) / (to.uy().x() * to.ux().y() - to.uy().y() * to.ux().x()) - to.origin().y();
    coordinate.x(ctx);
    coordinate.y(cty);
  }

  private static final boolean areEqual(double v1, double v2, double epsilon) {
    return v1 > v2 ? v1 - v2 <= epsilon : v2 - v1 <= epsilon;
  }


}