package com.hzyi.jplab.core.viewer;

import com.hzyi.jplab.core.model.Component;
import com.hzyi.jplab.core.model.ComponentState;

public interface Displayer {

  void display(Assembly assembly, AssemblyState state, DisplayContext context);

}