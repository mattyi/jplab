package com.hzyi.jplab.core.viewer;

import com.hzyi.jplab.core.model.Component;
import com.hzyi.jplab.core.model.ComponentState;
import com.hzyi.jplab.core.viewer.DisplayContext;
import java.util.function.BiFunction;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public abstract class JavaFxPainter implements Painter {

  private final BiFunction<Component, ComponentState, double[]> toPaintingParams;
  private final JavaFxDisplayer displayer;

  public JavaFxPainter(JavaFxDisplayer displayer, BiFunction<Component, ComponentState, double[]> toPaintingParams) {
    this.displayer = displayer;
    this.toPaintingParams = toPaintingParams;
  }

  protected Canvas getCanvas() {
    return this.displayer.getCanvas();
  }

  protected GraphicsContext getGraphicsContext() {
    return this.displayer.getCanvas().getGraphicsContext2D();
  }

  protected JavaFxDisplayer getDisplayer() {
    return this.displayer;
  }

  protected double[] getPaintingParams(Component component, ComponentState state) {
    return toPaintingParams.apply(component, state);
  }

  public abstract void paint(Component component, ComponentState state, DisplayContext context);
}