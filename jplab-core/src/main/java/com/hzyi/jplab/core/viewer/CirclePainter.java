package com.hzyi.jplab.core.viewer;

import com.google.common.base.Preconditions;
import com.hzyi.jplab.core.model.CircMassPoint;
import com.hzyi.jplab.core.model.Component;
import com.hzyi.jplab.core.model.ComponentState;
import com.hzyi.jplab.core.model.Field;
import com.hzyi.jplab.core.viewer.DisplayContext;
import com.hzyi.jplab.core.util.Coordinate;
import com.hzyi.jplab.core.util.Coordinates;
import com.hzyi.jplab.core.util.CoordinateSystem;
import javafx.scene.canvas.GraphicsContext;
import java.util.function.BiFunction;

public class CirclePainter extends JavaFxPainter {

  public CirclePainter(JavaFxDisplayer displayer, BiFunction<Component, ComponentState, double[]> toPaintingParams) {
    super(displayer, toPaintingParams);
  }

  @Override
  public void paint(
      Component component,
      ComponentState state,
      DisplayContext displayContext) {
    double[] params = getPaintingParams(component, state);
    paint(displayContext, params[0], params[1], params[2]);
  }

  private void paint(DisplayContext context, double x, double y, double r) {
    System.out.println("x: " + x);
    System.out.println("y: " + y);
    System.out.println("r: " + r);
    GraphicsContext graphicsContext = getGraphicsContext();
    DisplayUtil.graphicsContextColorAndStyle(graphicsContext, context);
    Coordinate origin = new Coordinate(x, y);
    Coordinate upperLeft = new Coordinate(origin.x() - r, origin.y() + r);
    Coordinate lowerRight = new Coordinate(origin.x() + r, origin.y() - r);
    upperLeft = Coordinates.transform(
        upperLeft,
        getDisplayer().getCoordinateTransformer().natural(),
        getDisplayer().getCoordinateTransformer().screen());
    lowerRight = Coordinates.transform(
        lowerRight,
        getDisplayer().getCoordinateTransformer().natural(),
        getDisplayer().getCoordinateTransformer().screen());
    System.out.println(upperLeft);
    System.out.println(lowerRight);
    System.out.println(getDisplayer().getCoordinateTransformer().natural());
    System.out.println(getDisplayer().getCoordinateTransformer().screen());
    double d = lowerRight.x() - upperLeft.x();
    System.out.println(context);
    switch (context.style()) {
      case stroke:
          graphicsContext.strokeOval(upperLeft.x(), upperLeft.y(), d, d);
          break;
      case fill:
          graphicsContext.fillOval(upperLeft.x(), upperLeft.y(), d, d);
          break;
      default: throw new IllegalArgumentException("Unknown style: " + context.style());
    }
  }
  
}