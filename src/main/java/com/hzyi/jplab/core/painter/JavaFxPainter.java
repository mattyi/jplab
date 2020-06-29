package com.hzyi.jplab.core.painter;

import com.hzyi.jplab.core.model.shape.Appearance;
import com.hzyi.jplab.core.util.Coordinate;
import javafx.scene.canvas.Canvas;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JavaFxPainter {

  private Canvas canvas;
  private CoordinateTransformer coordinateTransformer;

  public void drawLine(Coordinate from, Coordinate to, Appearance appearance) {
    applyAppearance(appearance);
    drawLine(from, to);
  }

  public void drawCircle(Coordinate center, double radius, Appearance appearance) {
    applyAppearance(appearance);
    switch (appearance.getStyle()) {
      case STROKE:
        strokeCircle(center, radius);
        return;
      case FILL:
        fillCircle(center, radius);
        return;
      default:
        throw new IllegalArgumentException("Unknown style: " + appearance.getStyle());
    }
  }

  private void drawLine(Coordinate from, Coordinate to) {
    from = coordinateTransformer.toScreen(from);
    to = coordinateTransformer.toScreen(to);
    canvas.getGraphicsContext2D().strokeLine(from.x(), from.y(), to.x(), to.y());
  }

  private void strokeCircle(Coordinate center, double radius) {
    Coordinate upperLeft = new Coordinate(center.x() - radius, center.y() + radius);
    upperLeft = coordinateTransformer.toScreen(upperLeft);
    radius = coordinateTransformer.toScreen(radius);
    canvas.getGraphicsContext2D().strokeOval(upperLeft.x(), upperLeft.y(), radius, radius);
  }

  private void fillCircle(Coordinate center, double radius) {
    Coordinate upperLeft = new Coordinate(center.x() - radius, center.y() + radius);
    upperLeft = coordinateTransformer.toScreen(upperLeft);
    radius = coordinateTransformer.toScreen(radius);
    canvas.getGraphicsContext2D().fillOval(upperLeft.x(), upperLeft.y(), radius, radius);
  }

  private void applyAppearance(Appearance appearance) {
    canvas.getGraphicsContext2D().setFill(toJavaFxColor(appearance.getColor()));
    canvas.getGraphicsContext2D().setStroke(toJavaFxColor(appearance.getColor()));
    canvas.getGraphicsContext2D().setLineWidth(appearance.getLineWidth());
  }

  private static javafx.scene.paint.Color toJavaFxColor(Appearance.Color color) {
    switch (color) {
      case RED:
        return javafx.scene.paint.Color.RED;
      case BLUE:
        return javafx.scene.paint.Color.BLUE;
      case YELLOW:
        return javafx.scene.paint.Color.YELLOW;
      case GREEN:
        return javafx.scene.paint.Color.GREEN;
      case BLACK:
        return javafx.scene.paint.Color.BLACK;
      case WHITE:
        return javafx.scene.paint.Color.WHITE;
      case GRAY:
        return javafx.scene.paint.Color.GRAY;
      default:
        throw new IllegalArgumentException("Unknown color: " + color);
    }
  }
}
