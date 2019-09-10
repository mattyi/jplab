package com.hzyi.jplab.core.viewer.shape;

import com.hzyi.jplab.core.viewer.CirclePainter;

public interface Line<T extends Line> extends Shape<T> {

  double length();

  double width();

}