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


public class JavaFxDisplayer implements Displayer, com.hzyi.jplab.core.util.Buildable {

  private final Canvas canvas;
  private final CoordinateTransformer transformer;

  private JavaFxDisplayer(Builder builder) {
    this.canvas = builder.canvas;
    this.transformer = builder.transformer;
  }

  public Canvas getCanvas() {
    return canvas;
  }

  public GraphicsContext getGraphicsContext() {
    return getCanvas().getGraphicsContext2D();
  }

  public CoordinateTransformer getCoordinateTransformer() {
    return transformer;
  }

  @Override
  public void display(Assembly assembly, AssemblyState state, DisplayContext context) {
    for (Component component : assembly.getComponents()) {
      String componentName = component.getName();
      ComponentState componentState = state.get(componentName);
      component.getPainter().paint(component, componentState, component.getDisplayContext());
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder implements com.hzyi.jplab.core.util.Builder<Builder> {
    
    private Canvas canvas;
    private CoordinateTransformer transformer;

    public Builder setCanvas(Canvas canvas) {
      this.canvas = canvas;
      return this;
    }

    public Builder setCoordinateTransformer(CoordinateTransformer transformer) {
      this.transformer = transformer;
      return this;
    }

    @Override
    public JavaFxDisplayer build() {
      return new JavaFxDisplayer(this);
    }
  }
}