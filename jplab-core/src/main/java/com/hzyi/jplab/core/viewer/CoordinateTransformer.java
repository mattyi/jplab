package com.hzyi.jplab.core.viewer;

import com.hzyi.jplab.core.controller.Parameter;
import com.hzyi.jplab.core.model.AssemblyState;
import com.hzyi.jplab.core.model.ComponentState;
import com.hzyi.jplab.core.model.Field;
import com.hzyi.jplab.core.util.CoordinateSystem;
import javafx.scene.canvas.Canvas;

public class CoordinateTransformer {
  
  private final Canvas canvas;
  private final AssemblyState assemblyState;
  private final double naturalRescaleRatio;
  private final CoordinateSystem natural = new CoordinateSystem(0, 0, 1, 0, 0, 1);
  private final CoordinateSystem screen = new CoordinateSystem(0, 0, 1, 0, 0, -1);

  public CoordinateTransformer(Canvas canvas, AssemblyState assemblyState) {
    this.canvas = canvas;
    this.assemblyState = assemblyState;
    canvas.widthProperty().addListener(
        (obs, oldVal, newVal) -> {
          screen.origin().x(((double) newVal) / -2.0);
        });
    canvas.heightProperty().addListener(
        (obs, oldVal, newVal) -> {
          screen.origin().y(((double) newVal) / 2.0);
        });
    naturalRescaleRatio = naturalRescaleRatio(assemblyState);
  }

  public CoordinateSystem natural() {
    return natural.ux(naturalRescaleRatio, 0).uy(0, naturalRescaleRatio);
  }

  public CoordinateSystem screen() {
    return screen;
  }

  // TODO: this is very rough right now. Make it more precise
  private static double naturalRescaleRatio(AssemblyState assemblyState) {
    double maxAbsLoc = 0;
    for (ComponentState componentState : assemblyState.getComponentStates()) {
      maxAbsLoc = 
          Math.max(
              Math.max(
                  maxAbsLoc,
                  Math.abs(componentState.get(Field.LOCX))), 
              Math.abs(componentState.get(Field.LOCY)));
    }
    return maxAbsLoc;
  }
}