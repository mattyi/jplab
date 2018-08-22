package com.hzyi.jplab.core.viewer;

import static com.google.common.base.Preconditions.checkArgument;
import static com.hzyi.jplab.core.util.Coordinates.transformInPlace;
import static com.hzyi.jplab.core.util.CoordinateSystem.natural;

import com.hzyi.jplab.core.model.Assembly;
import com.hzyi.jplab.core.model.AssemblyState;
import com.hzyi.jplab.core.model.Component;
import com.hzyi.jplab.core.model.ComponentState;
import com.hzyi.jplab.core.util.Coordinate;
import com.hzyi.jplab.core.util.CoordinateSystem;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;


public class JavaFxDisplayer implements Displayer {

  private static final Canvas CANVAS = new Canvas();
  private static final JavaFxDisplayer INSTANCE = new JavaFxDisplayer();

  private JavaFxDisplayer() {}

  public static JavaFxDisplayer getInstance() {
    return INSTANCE;
  }

  public JavaFxpainter newPainter(BiFunction<Component, ComponentState, double[]> infoExtractor) {
    return new JavaFxPainter(this, infoExtractor);
  }

  public Canvas getCanvas() {
    return CANVAS;
  }

  public GraphicsContext getGraphicsContext() {
    return getCanvas().getGraphicsContext2D();
  }

  public CoordinateSystem getCanvasCoordinateSystem() {
    return CoordinateSystem.screen(canvas.getWidth(), canvas.getHeight());
  }

  @Override
  public void display(Assembly assembly, AssemblyState state, DisplayContext context) {
    for (Component component : assembly.getComponent()) {
      String componentName = component.getName();
      ComponentState componentState = state.get(componentName);
      component.getPainter().paint(component, componentState, component.getDisplayContext());
    }
  }
}