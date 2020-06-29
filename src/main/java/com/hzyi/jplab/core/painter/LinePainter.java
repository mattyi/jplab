package com.hzyi.jplab.core.painter;

import com.hzyi.jplab.core.model.kinematic.Connector;
import com.hzyi.jplab.core.model.shape.Appearance;
import com.hzyi.jplab.core.model.shape.Line;
import com.hzyi.jplab.core.util.Coordinate;

public class LinePainter implements Painter<Connector, Line> {

  private final JavaFxPainter painter;

  LinePainter(JavaFxPainter painter) {
    this.painter = painter;
  }

  @Override
  public void paint(Line line, Connector model, Appearance appearance) {
    Coordinate pointU = model.pointU();
    Coordinate pointV = model.pointV();
    painter.drawLine(pointU, pointV, appearance);
  }
}
