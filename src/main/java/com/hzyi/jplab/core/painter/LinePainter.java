package com.hzyi.jplab.core.painter;

import com.hzyi.jplab.core.model.shape.Appearance;
import com.hzyi.jplab.core.model.shape.Line;
import com.hzyi.jplab.core.util.Coordinate;

public class LinePainter {

  private final JavaFxPainter painter;

  LinePainter(JavaFxPainter painter) {
    this.painter = painter;
  }

  public void paint(Coordinate pointU, Coordinate pointV, Line line, Appearance appearance) {
    painter.drawLine(pointU, pointV, appearance);
  }
}
