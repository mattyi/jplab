package com.hzyi.jplab.core.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.hzyi.jplab.core.viewer.Appearance;
import com.hzyi.jplab.core.viewer.shape.Circle;
import com.hzyi.jplab.core.viewer.Painter;
import com.hzyi.jplab.core.viewer.CirclePainter;

@Builder(builderMethodName = "newBuilder")
@Accessors(fluent = true)
public final class CircMassPoint extends MassPoint implements Circle {

  @Getter private String name;
  @Getter private double x;
  @Getter private double y;
  @Getter private double vx;
  @Getter private double vy;
  @Getter private double ax;
  @Getter private double ay;
  @Getter private double mass;
  @Getter private double radius;
  @Getter private Appearance appearance;
  @Getter @Setter private Assembly assembly;

  public ComponentState getInitialComponentState() {
    return new ComponentState(this)
        .put(Field.X, x)
        .put(Field.Y, y)
        .put(Field.VX, vx)
        .put(Field.VY, vy);
  }

  public String getName() {
    return name();
  }

  public void update(ComponentState state) {
    throw new UnsupportedOperationException();
  }

  public Painter getPainter() {
    return assembly.getPainterFactory().getCirclePainter();
  }

  public Appearance getAppearance() {
    return appearance();
  }

  public void paint() {
    getPainter().paint(this, x(), y(), 0);
  }
}