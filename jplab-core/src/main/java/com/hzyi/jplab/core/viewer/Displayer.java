package com.hzyi.jplab.core.viewer;

import com.hzyi.jplab.core.model.Assembly;
import com.hzyi.jplab.core.model.AssemblyState;

public interface Displayer {

  void display(Assembly assembly, AssemblyState state, DisplayContext context);

}