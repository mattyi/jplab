package com.hzyi.jplab.core.viewer.shape;

import com.hzyi.jplab.core.viewer.CirclePainter;

public interface ZigzagLine extends Shape<ZigzagLine> {

  double width();

  double length();

  int zigzagCount();

}