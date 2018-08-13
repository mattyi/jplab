package com.hzyi.jplab.core.viewer;

import com.hzyi.jplab.core.model.ComponentState;

public interface Displayer {

  display(ComponentState state);

  display(ComponentState state, DisplayContext context);

}