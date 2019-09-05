package com.hzyi.jplab.core.model;

import lombok.Getter;
import com.hzyi.jplab.core.viewer.Appearance;
import com.hzyi.jplab.core.viewer.Painter;
import com.hzyi.jplab.core.viewer.CirclePainter;

@Builder(builderMethodName = "newBuilder")
public final class CircMassPoint extends MassPoint implements Circle {

  @Getter private double x;
  @Getter private double y;
  @Getter private double vx;
  @Getter private double vy;
  @Getter private double ax;
  @Getter private double ay;
  @Getter private double mass;
  @Getter private double radius;
  @Getter private Appearance appearance;

  public getInitialComponentState() {
    return new ComponentState(this)
        .put(Field.X, x)
        .put(Field.Y, y)
        .put(Field.DIRX, dirX)
        .put(Field.DIRY, dirY)
        .put(Field.VX, vx)
        .put(Field.VY, vy);
  }

  public update(ComponentState state) {
    throw new UnsupportedOperationException();
  }

  public Painter getPainter() {
    return new CirclePainter();
  }
}