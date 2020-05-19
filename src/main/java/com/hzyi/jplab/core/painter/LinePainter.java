package com.hzyi.jplab.core.painter;

import com.hzyi.jplab.core.model.kinematic.Connector;
import com.hzyi.jplab.core.model.shape.Appearance;
import com.hzyi.jplab.core.model.shape.Line;
import com.hzyi.jplab.core.util.Coordinate;
import javafx.scene.canvas.Canvas;

public class LinePainter extends JavaFxPainter<Connector, Line> {

  LinePainter(Canvas canvas, CoordinateTransformer transformer) {
    super(canvas, transformer);
  }

  @Override
  public void paint(Line line, Connector model, Appearance appearance) {
    Coordinate pointU = model.pointU();
    Coordinate pointV = model.pointV();
    drawLine(pointU, pointV, appearance);
  }
}
