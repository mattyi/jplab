package com.hzyi.jplab.core.viewer;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class PainterFactory {

  @Getter private Canvas canvas;
  private CoordinateTransformer transformer;
  @Getter private final CirclePainter circlePainter;
  @Getter private final ZigzagLinePainter zigzagLinePainter;

  public PainterFactory(Canvas canvas, CoordinateTransformer transformer) {
    this.canvas = canvas;
    this.transformer = transformer;
    this.circlePainter = new CirclePainter(this.canvas, this.transformer);
    this.zigzagLinePainter = new ZigzagPainter(this.canvas, this.transformer);
  }

  public GraphicsContext getGraphicsContext() {
    return canvas.getGraphicsContext2D();
  }
}