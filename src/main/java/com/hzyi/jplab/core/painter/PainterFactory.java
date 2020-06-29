package com.hzyi.jplab.core.painter;

import javafx.scene.canvas.Canvas;
import lombok.Getter;

public class PainterFactory {

  @Getter private Canvas canvas;
  @Getter private CoordinateTransformer coordinateTransformer;
  @Getter private final CirclePainter circlePainter;
  @Getter private final ZigzagLinePainter zigzagLinePainter;
  @Getter private final EdgePainter edgePainter;
  @Getter private final LinePainter linePainter;
  @Getter private final CatenaryPainter catenaryPainter;

  public PainterFactory(Canvas canvas, CoordinateTransformer transformer) {
    JavaFxPainter javaFxPainter = new JavaFxPainter(canvas, transformer);
    this.canvas = canvas;
    this.circlePainter = new CirclePainter(javaFxPainter);
    this.zigzagLinePainter = new ZigzagLinePainter(javaFxPainter);
    this.edgePainter = new EdgePainter(javaFxPainter);
    this.linePainter = new LinePainter(javaFxPainter);
    this.catenaryPainter = new CatenaryPainter(javaFxPainter);
  }

  // For testing purpose.
  public static PainterFactory getTestingPainterFactory() {
    Canvas canvas = new Canvas(1, 1);
    return new PainterFactory(canvas, new CoordinateTransformer(canvas, 1.0));
  }

  public void clearCanvas() {
    double width = canvas.getWidth();
    double height = canvas.getHeight();
    canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
  }
}
