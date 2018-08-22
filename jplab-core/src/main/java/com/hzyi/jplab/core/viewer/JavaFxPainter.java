package com.hzyi.jplab.core.viewer;

import com.hzyi.jplab.core.model.Component;
import com.hzyi.jplab.core.model.ComponentState;
import com.hzyi.jplab.core.viewer.DisplayContext;

import java.util.function;

public abstract class JavaFxPainter implements Painter {

  private final BiFunction<Component, ComponentState, double[]> infoExtractor;
  private final JavaFxDisplayer displayer;

  public JavaFxPainter(JavaFxDisplayer displayer, BiFunction<Component, ComponentState, double[]> infoExtractor) {
    this.displayer = displayer;
    this.infoExtractor = infoExtractor;
  }

  protected Canvas getCanvas() {
    return this.displayer.getCanvas();
  }

  protected JavaFxDisplayer getDisplayer() {
    return this.displayer;
  }
  
  private void paint(double... info, DisplayContext context) {
    paint(getCanvas().getGraphicsContext2D(), info, context);
  }

  protected abstract void paint(GraphicsContext2D graphicsContext, double... info, DisplayContext context);

  public void paint(Component component, ComponentState state, DisplayContext context) {
    paint(infoExtractor.apply(component, state), context);
  }
  
}