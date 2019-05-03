package com.hzyi.jplab.core.viewer;

import com.hzyi.jplab.core.controller.Parameter;
import com.hzyi.jplab.core.model.AssemblyState;
import com.hzyi.jplab.core.model.ComponentState;
import com.hzyi.jplab.core.model.Field;
import com.hzyi.jplab.core.util.CoordinateSystem;
import javafx.scene.canvas.Canvas;

public class CoordinateTransformer {
  
  private final Canvas canvas;
  private final double naturalRescaleRatio;
  private final CoordinateSystem natural = new CoordinateSystem(0, 0, 1, 0, 0, 1);
  private final CoordinateSystem screen = new CoordinateSystem(0, 0, 1, 0, 0, -1);

  public CoordinateTransformer(Canvas canvas, double ratio) {
    this.canvas = canvas;
    canvas.widthProperty().addListener(
        (obs, oldVal, newVal) -> {
          screen.origin().x(((double) newVal) / -2.0);
        });
    canvas.heightProperty().addListener(
        (obs, oldVal, newVal) -> {
          screen.origin().y(((double) newVal) / 2.0);
        });
    naturalRescaleRatio = ratio;
  }

  public CoordinateSystem natural() {
    return natural.ux(naturalRescaleRatio, 0).uy(0, naturalRescaleRatio);
  }

  public CoordinateSystem screen() {
    return screen;
  }
}