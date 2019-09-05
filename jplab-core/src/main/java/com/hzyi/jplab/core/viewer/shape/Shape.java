package com.hzyi.jplab.core.viewer.shape;

import com.hzyi.jplab.core.viewer.Painter;
import com.hzyi.jplab.core.viewer.Appearance;

public interface Shape<T extends Shape> {

  public Painter<T> getPainter();

  public Appearance getAppearance();

}