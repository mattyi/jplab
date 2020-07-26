package com.hzyi.jplab.core.painter;

import com.hzyi.jplab.core.model.shape.Appearance;
import com.hzyi.jplab.core.model.shape.Circle;
import com.hzyi.jplab.core.util.Coordinate;

public class CirclePainter {

  private final JavaFxPainter painter;

  CirclePainter(JavaFxPainter painter) {
    this.painter = painter;
  }

  public void paint(Coordinate location, Circle circle, Appearance appearance) {
    painter.drawCircle(location, circle.radius(), appearance);
  }
}
