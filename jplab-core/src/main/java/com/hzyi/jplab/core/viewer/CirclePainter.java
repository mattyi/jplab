package com.hzyi.jplab.core.viewer;

import com.hzyi.jplab.core.model.Component;
import com.hzyi.jplab.core.model.ComponentState;
import com.hzyi.jplab.core.viewer.DisplayContext;

import java.util.function;

public class CirclePainter extends JavaFxPainter {

  BiFunction<Component, ComponentState, double[]> infoExtractor;

  public CirclePainter(BiFunction<Component, ComponentState, double[]> infoExtractor) {
    super(infoExtractor);
  }
  
  abstract void paint(double... info, DisplayContext context);

  public void paint(Component component, ComponentState state, DisplayContext context) {
    paint(infoExtractor.apply(component, state), context);
  }
  
}