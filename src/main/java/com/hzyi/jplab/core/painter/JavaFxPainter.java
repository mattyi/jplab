package com.hzyi.jplab.core.painter;

import com.hzyi.jplab.core.model.shape.Appearance;
import com.hzyi.jplab.core.model.shape.Shape;
import com.hzyi.jplab.core.model.kinematic.KinematicModel;
import com.hzyi.jplab.core.util.Coordinate;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public abstract class JavaFxPainter<K extends KinematicModel, S extends Shape>
    implements Painter<K, S> {

  private Canvas canvas;

  @Getter(AccessLevel.PROTECTED)
  private CoordinateTransformer coordinateTransformer;

  protected GraphicsContext getGraphicsContext() {
    return canvas.getGraphicsContext2D();
  }

  protected void drawLine(Coordinate from, Coordinate to) {
    applyAppearance(appearance);
    from = coordinateTransformer.toScreen(from);
    to = coordinateTransformer.toScreen(to);
    getGraphicsContext().strokeLine(from.x(), from.y(), to.x(), to.y());
  }

  protected void drawCircle(Coordinate center, double radius, Appearance appearance) {
    applyAppearance(appearance);
    switch (appearance.getStyle()) {
      case STROKE:
        return drawCircle(center, radius);
      case FILL:
        return fillCircle(center, radius);
      default:
        throw new IllegalArgumentException("Unknown style: " + circle.getAppearance().getStyle());
    }
  }

  private void strokeCircle(Coordinate center, double radius) {
    GraphicsContext graphicsContext = getGraphicsContext();
    DisplayUtil.graphicsContextColorAndStyle(graphicsContext, circle.getAppearance());
    Coordinate upperLeft = new Coordinate(center.x() - radius, center.y() + radius);
    upperLeft = coordinateTransformer.toScreen(upperLeft);
    getGraphicsContext().strokeOval(upperLeft.x(), upperLeft.y(), 2 * radius, 2 * radius);
  }

  private void fillCircle(Coordinate center, double radius) {
    GraphicsContext graphicsContext = getGraphicsContext();
    DisplayUtil.graphicsContextColorAndStyle(graphicsContext, circle.getAppearance());
    Coordinate upperLeft = new Coordinate(center.x() - radius, center.y() + radius);
    upperLeft = coordinateTransformer.toScreen(upperLeft);
    getGraphicsContext().fillOval(upperLeft.x(), upperLeft.y(), 2 * radius, 2 * radius);
  }

  private void applyAppearance(Appearance appearance) {
    getGraphicsContext().setFill(toJavaFxColor(appearance.getColor()));
    getGraphicsContext().setStroke(toJavaFxColor(appearance.getColor()));
    getGraphicsContext().setLineWidth(appearance.getLineWidth());
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
