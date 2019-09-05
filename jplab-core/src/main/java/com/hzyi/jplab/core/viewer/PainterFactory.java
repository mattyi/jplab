package com.hzyi.jplab.core.viewer;

import lombok.Builder;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

@Builder
public class PainterFactory {

  private Canvas canvas;
  @Getter private CoordinateTransformer transformer;

  public GraphicsContext getGraphicsContext() {
    return getCanvas().getGraphicsContext2D();
  }

  public newCirclePainter() {
    return new CirclePainter(this.canvas, this.transformer);
  }

}