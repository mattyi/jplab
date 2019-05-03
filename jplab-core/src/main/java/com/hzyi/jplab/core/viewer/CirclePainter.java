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
    GraphicsContext graphicsContext = getGraphicsContext();
    DisplayUtil.graphicsContextColorAndStyle(graphicsContext, context);
    Coordinate origin = new Coordinate(x, y);
    Coordinate to = origin;
    Coordinates.transform(
        origin,
        to,
        getDisplayer().getCoordinateTransformer().natural(),
        getDisplayer().getCoordinateTransformer().screen());
    double upperLeftX = origin.x() - r;
    double upperLeftY = origin.y() - r;
    double w = 2 * r;
    double h = 2 * r;
    switch (context.style()) {
      case stroke:
          graphicsContext.strokeOval(upperLeftX, upperLeftY, w, h);
          break;
      case fill:
          graphicsContext.fillOval(upperLeftX, upperLeftY, w, h);
      default: throw new IllegalArgumentException("Unknown style: " + context.style());
    }
  }
  
}