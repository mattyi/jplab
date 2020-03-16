package com.hzyi.jplab.core.painter;

import com.hzyi.jplab.core.model.kinematic.SingleKinematicModel;
import com.hzyi.jplab.core.model.shape.Circle;
import com.hzyi.jplab.core.util.Coordinate;
import javafx.scene.canvas.Canvas;

public class CirclePainter extends JavaFxPainter<SingleKinematicModel, Circle> {

  CirclePainter(Canvas canvas, CoordinateTransformer transformer) {
    super(canvas, transformer);
  }

  @Override
  public void paint(Circle circle, SingleKinematicModel model) {
    drawCircle(new Coordinate(model.x(), model.y()), circle.radius(), circle.getAppearance());
  }
}
