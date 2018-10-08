package com.hzyi.jplab.core.viewer;

import com.hzyi.jplab.core.model.Component;
import com.hzyi.jplab.core.model.ComponentState;
import com.hzyi.jplab.core.viewer.DisplayContext;
import com.hzyi.jplab.core.util.Coordinate;
import com.hzyi.jplab.core.util.Coordinates;
import com.hzyi.jplab.core.util.CoordinateSystem;
import javafx.scene.canvas.GraphicsContext;
import java.util.function.BiFunction;

public class CirclePainter extends JavaFxPainter {

  BiFunction<Component, ComponentState, double[]> infoExtractor;

  CirclePainter(JavaFxDisplayer displayer, BiFunction<Component, ComponentState, double[]> infoExtractor) {
    super(displayer, infoExtractor);
  }
  
  /*
   * @param info:
   * [0]: origin x
   * [1]: origin y
   * [2]: radius
   */
  @Override
  protected void paint(GraphicsContext graphicsContext, DisplayContext context, double... info) {
    DisplayUtil.graphicsContextColorAndStyle(graphicsContext, context);
    Coordinate origin = new Coordinate(info[0], info[1]);
    Coordinate to = origin;
    Coordinates.transform(
        origin,
        to,
        getDisplayer().getCoordinateTransformer().natural(),
        getDisplayer().getCoordinateTransformer().screen());
    double upperLeftX = origin.x() - info[2];
    double upperLeftY = origin.y() - info[2];
    double w = 2 * info[2];
    double h = 2 * info[2];
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