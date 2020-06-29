package com.hzyi.jplab.core.painter;

import com.hzyi.jplab.core.model.kinematic.SingleKinematicModel;
import com.hzyi.jplab.core.model.shape.Appearance;
import com.hzyi.jplab.core.model.shape.Circle;
import com.hzyi.jplab.core.util.Coordinate;

public class CirclePainter implements Painter<SingleKinematicModel, Circle> {

  private final JavaFxPainter painter;

  CirclePainter(JavaFxPainter painter) {
    this.painter = painter;
  }

  @Override
  public void paint(Circle circle, SingleKinematicModel model, Appearance appearance) {
    painter.drawCircle(new Coordinate(model.x(), model.y()), circle.radius(), appearance);
  }
}
