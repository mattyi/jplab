package com.hzyi.jplab.core.viewer.shape;

import com.hzyi.jplab.core.viewer.CirclePainter;

public interface Edge extends Shape<Edge> {

  double length();

  double innerLineAngle();

  double innerLineCount();

  double innerLineHeight();

}