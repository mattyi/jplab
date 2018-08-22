package com.hzyi.jplab.core.viewer;

import static com.google.common.base.Preconditions.checkArgument;
import static com.hzyi.jplab.core.util.CoordinateSystem.natural;

import com.hzyi.jplab.core.model.Assembly;
import com.hzyi.jplab.core.model.AssemblyState;
import com.hzyi.jplab.core.model.Component;
import com.hzyi.jplab.core.model.ComponentState;
import com.hzyi.jplab.core.util.Coordinate;
import com.hzyi.jplab.core.util.CoordinateSystem;
import java.util.function.BiFunction;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;


public class JavaFxDisplayer implements Displayer {

  private static final Canvas CANVAS = new Canvas();
  private static final JavaFxDisplayer INSTANCE = new JavaFxDisplayer();

  private JavaFxDisplayer() {}

  public static JavaFxDisplayer getInstance() {
    return INSTANCE;
  }

  public Canvas getCanvas() {
    return CANVAS;
  }

  public GraphicsContext getGraphicsContext() {
    return getCanvas().getGraphicsContext2D();
  }

  public CoordinateSystem getCanvasCoordinateSystem() {
    return CoordinateSystem.screen(CANVAS.getWidth(), CANVAS.getHeight());
  }

  @Override
  public void display(Assembly assembly, AssemblyState state, DisplayContext context) {
    for (Component component : assembly.getComponents()) {
      String componentName = component.getName();
      ComponentState componentState = state.get(componentName);
      component.getPainter().paint(component, componentState, component.getDisplayContext());
    }
  }
}