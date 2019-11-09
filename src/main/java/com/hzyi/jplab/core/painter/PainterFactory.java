package com.hzyi.jplab.core.painter;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import lombok.Getter;

public class PainterFactory {

  @Getter private Canvas canvas;
  private CoordinateTransformer transformer;
  @Getter private final CirclePainter circlePainter;
  @Getter private final ZigzagLinePainter zigzagLinePainter;
  @Getter private final EdgePainter edgePainter;

  public PainterFactory(Canvas canvas, CoordinateTransformer transformer) {
    this.canvas = canvas;
    this.transformer = transformer;
    this.circlePainter = new CirclePainter(this.canvas, this.transformer);
    this.zigzagLinePainter = new ZigzagLinePainter(this.canvas, this.transformer);
    this.edgePainter = new EdgePainter(this.canvas, this.transformer);
  }

  public GraphicsContext getGraphicsContext() {
    return canvas.getGraphicsContext2D();
  }
}
