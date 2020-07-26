package com.hzyi.jplab.core.painter;

import static com.google.common.base.Preconditions.checkState;

import com.google.common.annotations.VisibleForTesting;
import com.hzyi.jplab.core.model.shape.Appearance;
import com.hzyi.jplab.core.util.Coordinate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import lombok.Getter;

public class CatenaryPainter {

  // If set to true, CatenaryPainter will use the nearest approximation of a and o0 if
  // the maximum number of iteration is reached. Otherwise CatenaryPainter will throw
  // IllegalStateException.
  private static final boolean USE_NEAREST_APPX_AT_MAX_ITER = true;
  private static final double ERROR = 0.1;
  private static final int MAX_ITERATION_A = 20;
  private static final int MAX_ITERATION_X0 = 120;
  private static final int INTERPOLATION_POINT_COUNT = 20;

  private static final Map<String, Double> A_CACHE = new HashMap<String, Double>();
  private static final Map<String, Double> X_CACHE = new HashMap<String, Double>();

  @Getter private static int lastIterationCountForA; // For testing purpose
  @Getter private static int lastIterationCountForO0; // For testing purpose

  private final JavaFxPainter painter;

  CatenaryPainter(JavaFxPainter painter) {
    this.painter = painter;
  }

  public void paint(
      Coordinate pointU,
      Coordinate pointV,
      String name,
      double length,
      boolean isStretched,
      Appearance appearance) {
    if (pointU.x() > pointV.x()) {
      paint(pointV, pointU, name, length, isStretched, appearance);
      return;
    }
    if (isStretched) {
      painter.drawLine(pointU, pointV, appearance);
      return;
    }
    double a = a(pointU, pointV, length, name);
    Coordinate o0 = o0(pointU, pointV, name, a);
    Coordinate from = pointU;
    double x1 = pointU.x();
    double x2 = pointV.x();
    for (int i = 1; i <= INTERPOLATION_POINT_COUNT; i++) {
      double x = x1 + (double) i * (x2 - x1) / (double) INTERPOLATION_POINT_COUNT;
      Coordinate to = new Coordinate(x, catenary(a, o0).apply(x));
      painter.drawLine(from, to, appearance);
      from = to;
    }
  }

  @VisibleForTesting
  static double a(Coordinate pointU, Coordinate pointV, double l0, String name) {
    // using a binary search to calculate a as a satisfies: sqrt(s2 - v2) = 2a * sinh(h/2a)
    // The function is a monotonic decreasing function
    // also assume pointU.x <= pointV.x is always true
    double x1 = pointU.x(), x2 = pointV.x();
    double y1 = pointU.y(), y2 = pointV.y();
    double dh = Math.abs(x1 - x2);
    double lhs = Math.sqrt(l0 * l0 - (y1 - y2) * (y1 - y2));
    Function<Double, Double> rhs = rhs(dh);

    // Start the binary search from a range around the cached value
    double a0 = A_CACHE.getOrDefault(name, 0.1);
    double lower = a0 / 2.0;
    double higher = a0 * 2.0;
    int i = 0;
    while (rhs.apply(lower) < lhs) {
      if (i >= MAX_ITERATION_A) {
        return nearestAppxOrThrow(lower);
      }
      higher = lower;
      lower /= 2.0;
      i++;
    }
    while (rhs.apply(higher) > lhs) {
      if (i >= MAX_ITERATION_A) {
        return nearestAppxOrThrow(higher);
      }
      lower = higher;
      higher *= 2;
      i++;
    }
    double mid = (lower + higher) / 2;
    while (Math.abs(rhs.apply(mid) - lhs) > ERROR) {
      if (i >= MAX_ITERATION_A) {
        return nearestAppxOrThrow(mid);
      }
      if (rhs.apply(mid) > lhs) {
        lower = mid;
      } else {
        higher = mid;
      }
      mid = (lower + higher) / 2.0;
      i++;
    }
    A_CACHE.put(name, mid);
    lastIterationCountForA = i;
    return mid;
  }

  @VisibleForTesting
  static Coordinate o0(Coordinate pointU, Coordinate pointV, String name, double a) {
    double x0 = x0(pointU, pointV, name, a);
    double y0 = pointU.y() - catenary(a).apply(pointU.x() - x0);
    return new Coordinate(x0, y0);
  }

  private static double x0(Coordinate pointU, Coordinate pointV, String name, double a) {
    // first, use a binary search to calculate x0 as x0 satisfies:
    // y1 - y2 = catenary(x1 - x0) - catenary(x2 - x0) (monotonic increasing)
    // where catenary is the curve function of the catenary.
    // also assume pointU.x <= pointV.x is always true
    double x0d = (pointU.x() + pointV.x()) / 2.0;
    double x0 = X_CACHE.getOrDefault(name, x0d);
    Function<Double, Double> catenary = catenary(a);
    Function<Double, Double> rhs =
        x -> (catenary.apply(pointU.x() - x) - catenary.apply(pointV.x() - x));
    double lhs = pointU.y() - pointV.y();
    double lower = x0 - (pointV.x() - pointU.x()) / 2;
    double higher = x0 + (pointV.x() - pointU.x()) / 2;
    int i = 0;
    while (rhs.apply(lower) > lhs) {
      if (i >= MAX_ITERATION_X0) {
        // return nearestAppxOrThrow(lower);
        checkState(false);
      }
      higher = lower;
      lower -= (x0 - lower);
      i++;
    }
    while (rhs.apply(higher) < lhs) {
      if (i >= MAX_ITERATION_X0) {
        // return nearestAppxOrThrow(higher);
        checkState(false);
      }
      lower = higher;
      higher += (higher - x0);
      i++;
    }
    double mid = (lower + higher) / 2;
    while (Math.abs(rhs.apply(mid) - lhs) > ERROR) {
      if (i == MAX_ITERATION_X0) {
        // return nearestAppxOrThrow(mid);
        checkState(false);
      }
      if (rhs.apply(mid) < lhs) {
        lower = mid;
      } else {
        higher = mid;
      }
      mid = (lower + higher) / 2.0;
      i++;
    }
    X_CACHE.put(name, mid);
    lastIterationCountForO0 = i;
    return mid;
  }

  private static double nearestAppxOrThrow(double appx) {
    if (USE_NEAREST_APPX_AT_MAX_ITER) {
      return appx;
    }
    throw new IllegalStateException(String.format("max iteration reached: %d", MAX_ITERATION_A));
  }

  private static Function<Double, Double> rhs(final double h) {
    return a -> (2.0 * a * Math.sinh(h / 2.0 / a));
  }

  private static Function<Double, Double> catenary(final double a) {
    return x -> (a * Math.cosh(x / a));
  }

  private static Function<Double, Double> catenary(double a, Coordinate o0) {
    return x -> (a * Math.cosh((x - o0.x()) / a) + o0.y());
  }
}
