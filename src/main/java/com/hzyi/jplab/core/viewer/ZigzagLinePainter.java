package com.hzyi.jplab.core.viewer;

import com.google.common.base.Preconditions;
import com.hzyi.jplab.core.model.CircMassPoint;
import com.hzyi.jplab.core.viewer.shape.ZigzagLine;
import com.hzyi.jplab.core.model.ComponentState;
import com.hzyi.jplab.core.model.Field;
import com.hzyi.jplab.core.viewer.Appearance;
import com.hzyi.jplab.core.util.Coordinate;
import com.hzyi.jplab.core.util.Coordinates;
import com.hzyi.jplab.core.util.CoordinateSystem;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;
import javafx.scene.shape.Line;

public class ZigzagLinePainter extends JavaFxPainter<ZigzagLine> {

  ZigzagLinePainter(Canvas canvas, CoordinateTransformer transformer) {
    super(canvas, transformer);
  }

  @Override
  public void paint(
      ZigzagLine line, double x, double y, double theta) {
    GraphicsContext graphicsContext = getGraphicsContext();
    DisplayUtil.graphicsContextColorAndStyle(graphicsContext, line.getAppearance());
    Coordinate connectingPointA = 
        new Coordinate(
            x - line.length() * Math.cos(theta) * 0.5,
            y - line.length() * Math.sin(theta) * 0.5);
    Coordinate connectingPointB = 
        new Coordinate(
            x + line.length() * Math.cos(theta) * 0.5,
            y + line.length() + Math.sin(theta) * 0.5);

    x = connectingPointA.x();
    y = connectingPointA.y();
    double zigzagLength = line.length() / (line.zigzagCount() + 1);
    Coordinate previous = new Coordinate(x, y);
    boolean clockwise = true;

    // the first zigzag only takes up half the length
    Coordinate next = getZigzagBoundaryPoint(connectingPointA, line.width() / 2, theta, zigzagLength / 2, clockwise);
    for (int i = 0; i <= line.zigzagCount(); i++) {
      drawLine(previous, next);
      previous = next;
      clockwise = !clockwise;
      next = getZigzagBoundaryPoint(previous, line.width(), theta, zigzagLength, clockwise);
    }
  }

  private static Coordinate getZigzagBoundaryPoint(Coordinate start, double width, double theta, double lengthFromStart, boolean clockwise) {
    // First, move from A along the zigzag line by lengthFromStart
    Coordinate destination = new Coordinate(lengthFromStart * Math.cos(theta), lengthFromStart * Math.sin(theta));
    destination.x(destination.x() + start.x());
    destination.y(destination.y() + start.y());

    // Secondly, we move to the edge of the line
    theta = theta + Math.PI / 2.0 * (clockwise ? 1 : -1);
    destination.x(destination.x() + width * Math.cos(theta));
    destination.y(destination.y() + width * Math.sin(theta));

    return destination;
  }
}