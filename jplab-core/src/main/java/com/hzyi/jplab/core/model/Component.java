package com.hzyi.jplab.core.model;

import com.hzyi.jplab.core.viewer.Painter;
import com.hzyi.jplab.core.viewer.DisplayContext;

public interface Component {

  public ComponentState getInitialComponentState();

  public String getName();

  public Painter getPainter();

  public DisplayContext getDisplayContext();

}