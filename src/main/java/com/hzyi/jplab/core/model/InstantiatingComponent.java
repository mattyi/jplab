package com.hzyi.jplab.core.model;

import static com.google.common.base.MoreObjects.firstNonNull;
import static com.google.common.base.Preconditions.checkNotNull;

import com.hzyi.jplab.core.model.kinematic.KinematicModel;
import com.hzyi.jplab.core.model.shape.Appearance;
import com.hzyi.jplab.core.model.shape.Shape;
import com.hzyi.jplab.core.painter.Painter;
import lombok.Getter;

public class InstantiatingComponent<
        K extends KinematicModel, S extends Shape, P extends Painter<K, S>>
    implements Component<K, S> {

  @Getter private final String name;
  @Getter private final S shape;
  @Getter private final Appearance appearance;
  @Getter private final P painter;
  @Getter private final K initialKinematicModel;

  public InstantiatingComponent(
      String name, K kinematicModel, S shape, P painter, Appearance appearance) {
    this.name = checkNotNull(name);
    this.initialKinematicModel = checkNotNull(kinematicModel);
    this.shape = checkNotNull(shape);
    this.painter = checkNotNull(painter);
    this.appearance = firstNonNull(appearance, Appearance.of());
  }
}
