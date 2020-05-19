package com.hzyi.jplab.core.painter;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import lombok.Getter;

public class PainterFactory {

  @Getter private Canvas canvas;
  @Getter private CoordinateTransformer coordinateTransformer;
  @Getter private final CirclePainter circlePainter;
  @Getter private final ZigzagLinePainter zigzagLinePainter;
  @Getter private final EdgePainter edgePainter;
  @Getter private final LinePainter linePainter;

  public PainterFactory(Canvas canvas, CoordinateTransformer transformer) {
    this.canvas = canvas;
    this.coordinateTransformer = transformer;
    this.circlePainter = new CirclePainter(this.canvas, this.coordinateTransformer);
    this.zigzagLinePainter = new ZigzagLinePainter(this.canvas, this.coordinateTransformer);
    this.edgePainter = new EdgePainter(this.canvas, this.coordinateTransformer);
    this.linePainter = new LinePainter(this.canvas, this.coordinateTransformer);
  }

  // For testing purpose.
  public static PainterFactory getTestingPainterFactory() {
    Canvas canvas = new Canvas(1, 1);
    return new PainterFactory(canvas, new CoordinateTransformer(canvas, 1.0));
  }

  public GraphicsContext getGraphicsContext() {
    return canvas.getGraphicsContext2D();
  }

  public void clearCanvas() {
    double canvasWidth = canvas.getWidth();
    double canvasHeight = canvas.getHeight();
    getGraphicsContext().clearRect(0, 0, canvasWidth, canvasHeight);
  }
}
