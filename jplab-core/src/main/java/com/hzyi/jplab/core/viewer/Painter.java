package com.hzyi.jplab.core.viewer;

import com.hzyi.jplab.core.model.Component;
import com.hzyi.jplab.core.model.ComponentState;

public interface Painter {

  void paint(Component component, ComponentState state, DisplayContext context);
  
}