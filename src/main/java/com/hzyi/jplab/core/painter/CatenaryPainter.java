package com.hzyi.jplab.core.painter;

import static com.google.common.base.Preconditions.checkState;

import com.google.common.annotations.VisibleForTesting;
import com.hzyi.jplab.core.model.kinematic.RopeModel;
import com.hzyi.jplab.core.model.shape.Appearance;
import com.hzyi.jplab.core.model.shape.Catenary;
import com.hzyi.jplab.core.util.Coordinate;
import com.hzyi.jplab.core.util.Coordinates;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javafx.scene.canvas.Canvas;
import lombok.Getter;

public class CatenaryPainter extends JavaFxPainter<RopeModel, Catenary> {

  private static final double ERROR = 0.001;
  private static final int MAX_ITERATION = 20;
  private static final int INTERPOLATION_POINT_COUNT = 10;

  private static final Map<String, Double> A_CACHE = new HashMap<String, Double>();
  private static final Map<String, Double> X_CACHE = new HashMap<String, Double>();

  @Getter private static int lastIterationCountForA; // For testing purpose
  @Getter private static int lastIterationCountForO0; // For testing purpose

  CatenaryPainter(Canvas canvas, CoordinateTransformer transformer) {
    super(canvas, transformer);
  }

  @Override
  public void paint(Catenary catenary, RopeModel model, Appearance appearance) {
    Coordinate pointU = model.pointU();
    Coordinate pointV = model.pointV();
    double length = Coordinates.distance(pointU, pointV);
    if (length > model.length()) {
      drawLine(pointU, pointV, appearance);
    }
    drawCatenary(pointU, pointV, model, appearance);
  }

  private void drawCatenary(
      Coordinate pointU, Coordinate pointV, RopeModel model, Appearance appearance) {
    if (pointU.x() > pointV.x()) {
      drawCatenary(pointV, pointU, model, appearance);
      return;
    }
    double a = a(pointU, pointV, model.length(), model.name());
    Coordinate o0 = o0(pointU, pointV, model.name(), a);
    Coordinate from = pointU;
    double x1 = pointU.x();
    double x2 = pointV.x();
    for (int i = 1; i <= INTERPOLATION_POINT_COUNT; i++) {
      double x = x1 + (double) i * (x2 - x1) / (double) INTERPOLATION_POINT_COUNT;
      Coordinate to = new Coordinate(x, catenary(a, o0).apply(x));
      drawLine(from, to, appearance);
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
      checkState(i < MAX_ITERATION, "reached max iteration: %d", i);
      higher = lower;
      lower /= 2.0;
      i++;
    }
    while (rhs.apply(higher) > lhs) {
      checkState(i < MAX_ITERATION, "reached max iteration: %d", i);
      lower = higher;
      higher *= 2;
      i++;
    }
    double mid = (lower + higher) / 2;
    while (Math.abs(rhs.apply(mid) - lhs) > ERROR) {
      checkState(i < MAX_ITERATION, "reached max iteration: %d", i);
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
      checkState(i < MAX_ITERATION, "reached max iteration: %d", i);
      higher = lower;
      lower -= (x0 - lower);
      i++;
    }
    while (rhs.apply(higher) < lhs) {
      checkState(i < MAX_ITERATION, "reached max iteration: %d", i);
      lower = higher;
      higher += (higher - x0);
      i++;
    }
    double mid = (lower + higher) / 2;
    while (Math.abs(rhs.apply(mid) - lhs) > ERROR) {
      checkState(i < MAX_ITERATION, "reached max iteration: %d", i);
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
    double y0 = pointU.y() - catenary(a).apply(pointU.x() - mid);
    return new Coordinate(mid, y0);
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
