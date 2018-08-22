package com.hzyi.jplab.core.viewer;

import com.hzyi.jplab.core.model.Component;
import com.hzyi.jplab.core.model.ComponentState;
import com.hzyi.jplab.core.viewer.DisplayContext;
import java.util.function.BiFunction;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

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
  
  private void paint(DisplayContext context, double... info) {
    paint(getCanvas().getGraphicsContext2D(), context, info);
  }

  protected abstract void paint(GraphicsContext graphicsContext, DisplayContext context, double... info);

  public void paint(Component component, ComponentState state, DisplayContext context) {
    paint(context, infoExtractor.apply(component, state));
  }
  
}