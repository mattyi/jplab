package com.hzyi.jplab.core.painter;

import com.hzyi.jplab.core.model.kinematic.ConnectingModel;
import com.hzyi.jplab.core.model.shape.Appearance;
import com.hzyi.jplab.core.model.shape.ZigzagLine;
import com.hzyi.jplab.core.util.Coordinate;
import com.hzyi.jplab.core.util.Coordinates;
import javafx.scene.canvas.Canvas;

public class ZigzagLinePainter extends JavaFxPainter<ConnectingModel, ZigzagLine> {

  ZigzagLinePainter(Canvas canvas, CoordinateTransformer transformer) {
    super(canvas, transformer);
  }

  @Override
  @Deprecated
  public void paint(ZigzagLine line, ConnectingModel model) {
    paint(line, model, line.getAppearance());
  }

  @Override
  public void paint(ZigzagLine line, ConnectingModel model, Appearance appearance) {
    Coordinate connectingPointA = model.absoluteConnectingPointA();
    Coordinate connectingPointB = model.absoluteConnectingPointB();
    double x = connectingPointA.x();
    double y = connectingPointA.y();
    double length = Coordinates.distance(connectingPointA, connectingPointB);
    double zigzagLength = length / (line.zigzagCount() + 1);
    Coordinate previous = connectingPointA;
    boolean clockwise = true;

    // the first zigzag only takes up half the length
    Coordinate next =
        getZigzagBoundaryPoint(
            connectingPointA, line.width() / 2, model.theta(), zigzagLength / 2, clockwise);
    for (int i = 0; i <= line.zigzagCount(); i++) {
      drawLine(previous, next, appearance);
      previous = next;
      clockwise = !clockwise;
      next = getZigzagBoundaryPoint(previous, line.width(), model.theta(), zigzagLength, clockwise);
    }
    drawLine(previous, connectingPointB, appearance);
  }

  private static Coordinate getZigzagBoundaryPoint(
      Coordinate start, double width, double theta, double lengthFromStart, boolean clockwise) {
    // First, move from A along the zigzag line by lengthFromStart
    Coordinate destination =
        new Coordinate(lengthFromStart * Math.cos(theta), lengthFromStart * Math.sin(theta));
    destination.x(destination.x() + start.x());
    destination.y(destination.y() + start.y());

    // Secondly, we move to the edge of the line
    theta = theta + Math.PI / 2.0 * (clockwise ? 1 : -1);
    destination.x(destination.x() + width * Math.cos(theta));
    destination.y(destination.y() + width * Math.sin(theta));

    return destination;
  }
}
