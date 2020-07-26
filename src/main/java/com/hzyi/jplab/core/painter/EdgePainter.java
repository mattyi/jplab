package com.hzyi.jplab.core.painter;

import com.hzyi.jplab.core.model.shape.Appearance;
import com.hzyi.jplab.core.model.shape.Edge;
import com.hzyi.jplab.core.util.Coordinate;

public class EdgePainter {

  private final JavaFxPainter painter;

  EdgePainter(JavaFxPainter painter) {
    this.painter = painter;
  }

  public void paint(double x, double y, double theta, Edge edge, Appearance appearance) {
    double endAX = x + edge.length() * Math.cos(theta + (Math.PI / 2.0)) / 2;
    double endAY = y + edge.length() * Math.sin(theta + (Math.PI / 2.0)) / 2;
    double endBX = x * 2 - endAX;
    double endBY = y * 2 - endAY;
    painter.drawLine(new Coordinate(endAX, endAY), new Coordinate(endBX, endBY), appearance);

    Coordinate start = new Coordinate(0, 0);
    Coordinate end = new Coordinate(0, 0);
    for (int i = 1; i <= edge.innerLineCount(); i++) {
      start.x(i * (endBX - endAX) / (edge.innerLineCount() + 1) + endAX);
      start.y(i * (endBY - endAY) / (edge.innerLineCount() + 1) + endAY);
      end.x(start.x() + edge.innerLineHeight() * -Math.sin(edge.innerLineAngle() + theta));
      end.y(start.y() + edge.innerLineHeight() * -Math.cos(edge.innerLineAngle() + theta));
      painter.drawLine(start, end, appearance);
    }
  }
}
