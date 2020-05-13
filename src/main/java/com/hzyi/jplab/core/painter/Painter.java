package com.hzyi.jplab.core.painter;

import com.hzyi.jplab.core.model.kinematic.KinematicModel;
import com.hzyi.jplab.core.model.shape.Appearance;
import com.hzyi.jplab.core.model.shape.Shape;

public interface Painter<K extends KinematicModel, S extends Shape> {

  @Deprecated
  default void paint(S shape, K model) {
    paint(shape, model, Appearance.of());
  }

  void paint(S shape, K model, Appearance appearance);
}
