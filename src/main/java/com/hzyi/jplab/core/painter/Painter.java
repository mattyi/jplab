package com.hzyi.jplab.core.painter;

import com.hzyi.jplab.core.model.kinematic.KinematicModel;
import com.hzyi.jplab.core.model.shape.Shape;

public interface Painter<K extends KinematicModel, S extends Shape> {

  void paint(T shape, K model);
}
