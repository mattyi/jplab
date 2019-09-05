package com.hzyi.jplab.core.viewer;

import com.hzyi.jplab.core.model.ComponentState;

public interface Painter<T extends Shape> {

  void paint(T shape, double x, double y, double dirx, double diry);
  
}