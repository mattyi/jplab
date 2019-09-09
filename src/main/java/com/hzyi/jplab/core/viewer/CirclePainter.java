package com.hzyi.jplab.core.viewer;

import com.google.common.base.Preconditions;
import com.hzyi.jplab.core.model.CircMassPoint;
import com.hzyi.jplab.core.viewer.shape.Circle;
import com.hzyi.jplab.core.model.ComponentState;
import com.hzyi.jplab.core.model.Field;
import com.hzyi.jplab.core.viewer.Appearance;
import com.hzyi.jplab.core.util.Coordinate;
import com.hzyi.jplab.core.util.Coordinates;
import com.hzyi.jplab.core.util.CoordinateSystem;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;

public class CirclePainter extends JavaFxPainter<Circle> {

  CirclePainter(Canvas canvas, CoordinateTransformer transformer) {
    super(canvas, transformer);
  }

  @Override
  public void paint(
      Circle circle, double x, double y, double theta) {
    paint(circle, x, y, 0, 0);
  }



  private void paint(
      Circle circle, double x, double y, double dirx, double diry) {
    GraphicsContext graphicsContext = getGraphicsContext();
    DisplayUtil.graphicsContextColorAndStyle(graphicsContext, circle.getAppearance());
    double r = circle.radius();
    Coordinate origin = new Coordinate(x, y);
    Coordinate upperLeft = new Coordinate(origin.x() - r, origin.y() + r);
    Coordinate lowerRight = new Coordinate(origin.x() + r, origin.y() - r);
    upperLeft = Coordinates.transform(
        upperLeft,
        getCoordinateTransformer().natural(),
        getCoordinateTransformer().screen());
    lowerRight = Coordinates.transform(
        lowerRight,
        getCoordinateTransformer().natural(),
        getCoordinateTransformer().screen());
    double d = lowerRight.x() - upperLeft.x();
    switch (circle.getAppearance().getStyle()) {
      case stroke:
          graphicsContext.strokeOval(upperLeft.x(), upperLeft.y(), d, d);
          break;
      case fill:
          graphicsContext.fillOval(upperLeft.x(), upperLeft.y(), d, d);
          break;
      default: throw new IllegalArgumentException("Unknown style: " + circle.getAppearance().getStyle());
    }
  }
  
}