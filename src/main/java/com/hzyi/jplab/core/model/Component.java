package com.hzyi.jplab.core.model;

import com.hzyi.jplab.core.model.kinematic.KinematicModel;
import com.hzyi.jplab.core.model.shape.Appearance;
import com.hzyi.jplab.core.model.shape.Shape;
import com.hzyi.jplab.core.painter.Painter;

public interface Component<K extends KinematicModel, S extends Shape> {

  String getName();

  K getInitialKinematicModel();

  default Appearance getAppearance() {
    return Appearance.of();
  }

  default K getKinematicModel(AssemblySnapshot assemblySnapshot) {
    return (K) (assemblySnapshot.get(getName()));
  }

  S getShape();

  Painter<K, S> getPainter();

  default void paint(K kinematicModel) {
    getPainter().paint(getShape(), kinematicModel, getAppearance());
  }
}
