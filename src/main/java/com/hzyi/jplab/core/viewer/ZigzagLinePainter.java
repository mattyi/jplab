package com.hzyi.jplab.core.viewer;

import com.google.common.base.Preconditions;
import com.hzyi.jplab.core.model.CircMassPoint;
import com.hzyi.jplab.core.viewer.shape.ZigzagLine;
import com.hzyi.jplab.core.model.ComponentState;
import com.hzyi.jplab.core.model.Field;
import com.hzyi.jplab.core.viewer.Appearance;
import com.hzyi.jplab.core.util.Coordinate;
import com.hzyi.jplab.core.util.Coordinates;
import com.hzyi.jplab.core.util.CoordinateSystem;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;
import java.util.Math;
import javafx.scene.shape.Line;

public class ZigzagPainter extends JavaFxPainter<Circle> {

  ZigzagPainter(Canvas canvas, CoordinateTransformer transformer) {
    super(canvas, transformer);
  }

  @Override
  public void paint(
      ZigzagLine line, double x, double y, double theta) {
    Coordinate connectingPointA = 
        new Coordinate(
            x - length() * Math.cos(theta) * 0.5,
            y - length() * Math.sin(theta) * 0.5);
    Coordinate connectingPointB = 
        new Coordinate(
            x + length() * Math.cos(theta) * 0.5,
            y + length() + Math.sin(theta) * 0.5);
    connectingPointA = Coordinates.transform(
        connectingPointA,
        getCoordinateTransformer().natural(),
        getCoordinateTransformer().screen());
    connectingPointB = Coordinates.transform(
        connectingPointB,
        getCoordinateTransformer().natural(),
        getCoordinateTransformer().screen());
    x = connectingPointA.x();
    y = connectingPointA.y();
    for (int i = 0.5; i < ling.zigzagCount(); i++) {
      Line segment = new Line();
      segment.setStartX(x);
      segment.setStartY(y);
      x = x + ;
      y = y + ;
    }
  }

}