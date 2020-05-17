package com.hzyi.jplab.core.painter;

import com.hzyi.jplab.core.model.kinematic.ConnectingModel;
import com.hzyi.jplab.core.model.shape.Appearance;
import com.hzyi.jplab.core.model.shape.Line;
import com.hzyi.jplab.core.util.Coordinate;
import javafx.scene.canvas.Canvas;

public class LinePainter extends JavaFxPainter<ConnectingModel, Line> {

  LinePainter(Canvas canvas, CoordinateTransformer transformer) {
    super(canvas, transformer);
  }

  @Override
  public void paint(Line line, ConnectingModel model, Appearance appearance) {
    Coordinate connectingPointA = model.absoluteConnectingPointA();
    Coordinate connectingPointB = model.absoluteConnectingPointB();
    drawLine(connectingPointA, connectingPointB, appearance);
  }
}
