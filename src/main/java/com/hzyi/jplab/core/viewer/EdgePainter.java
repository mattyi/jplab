package com.hzyi.jplab.core.viewer;

import com.google.common.base.Preconditions;
import com.hzyi.jplab.core.model.CircMassPoint;
import com.hzyi.jplab.core.viewer.shape.Edge;
import com.hzyi.jplab.core.model.Field;
import com.hzyi.jplab.core.viewer.Appearance;
import com.hzyi.jplab.core.util.Coordinate;
import com.hzyi.jplab.core.util.Coordinates;
import com.hzyi.jplab.core.util.CoordinateSystem;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;
import javafx.scene.shape.Line;

public class EdgePainter extends JavaFxPainter<Edge> {
  EdgePainter(Canvas canvas, CoordinateTransformer transformer) {
    super(canvas, transformer);
  }

  @Override
  public void paint(
      Edge edge, double x, double y, double theta) {
    GraphicsContext graphicsContext = getGraphicsContext();
    DisplayUtil.graphicsContextColorAndStyle(graphicsContext, edge.getAppearance());
    double endAX = x + edge.length() * Math.cos(theta + (Math.PI / 2.0)) / 2;
    double endAY = y + edge.length() * Math.sin(theta + (Math.PI / 2.0)) / 2;
    double endBX = x * 2 - endAX;
    double endBY = y * 2 - endAY;
    drawLine(new Coordinate(endAX, endAY), new Coordinate(endBX, endBY));

    Coordinate start = new Coordinate(0, 0);
    Coordinate end = new Coordinate(0, 0);
    for (int i = 1; i <= edge.innerLineCount(); i++) {
      start.x(i * (endBX - endAX) / (edge.innerLineCount() + 1) + endAX);
      start.y(i * (endBY - endAY) / (edge.innerLineCount() + 1) + endAY);
      end.x(start.x() + edge.innerLineHeight() * Math.cos(edge.innerLineAngle()));
      end.y(start.y() + edge.innerLineHeight() * Math.sin(edge.innerLineAngle()));
      drawLine(start, end);
    }
  }
}