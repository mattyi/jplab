package com.hzyi.jplab.core.viewer;

import com.hzyi.jplab.core.viewer.shape.Shape;
import com.hzyi.jplab.core.model.ComponentState;
import com.hzyi.jplab.core.viewer.Appearance;
import com.hzyi.jplab.core.util.Coordinate;
import com.hzyi.jplab.core.util.Coordinates;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.AccessLevel;

@AllArgsConstructor
public abstract class JavaFxPainter<T extends Shape> implements Painter<T> {

  private Canvas canvas;
  @Getter(AccessLevel.PROTECTED) private CoordinateTransformer coordinateTransformer;

  protected GraphicsContext getGraphicsContext() {
    return canvas.getGraphicsContext2D();
  }

  public abstract void paint(T shape, double x, double y, double theta);

  protected void drawLine(Coordinate naturalA, Coordinate naturalB) {
    Coordinate screenA = Coordinates.transform(
        naturalA,
        getCoordinateTransformer().natural(),
        getCoordinateTransformer().screen());
    Coordinate screenB = Coordinates.transform(
        naturalB,
        getCoordinateTransformer().natural(),
        getCoordinateTransformer().screen());
    getGraphicsContext().strokeLine(screenA.x(), screenA.y(), screenB.x(), screenB.y());
  }
}