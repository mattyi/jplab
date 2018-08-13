package com.hzyi.jplab.core.viewer;

import static com.google.common.base.Preconditions.checkArgument;

import javafx.scene.canvas.GraphicsContext;


public class CircDisplayer implements Displayer {

  private final GraphicsContext graphicsContext;

  public CircDisplayer(GraphicsContext context) {
    this.graphicsContext = context;
  }

  @Override
  public void display(ComponentState state, DisplayContext context) {
    
  }

  @Override
  public void display(ComponentState state) {

  }

}