package com.hzyi.jplab.core.model;

import com.hzyi.jplab.core.model.kinematic.MassPoint;
import com.hzyi.jplab.core.model.shape.Appearance;
import com.hzyi.jplab.core.model.shape.Circle;
import com.hzyi.jplab.core.painter.Painter;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder(builderMethodName = "newBuilder")
public final class CircleMassPoint implements Component<MassPoint, Circle> {

  @Getter private String name;
  private double x;
  private double y;
  private double vx;
  private double vy;
  private double ax;
  private double ay;
  private double mass;
  private double radius;
  private Appearance appearance;
  @Getter @Setter private Assembly assembly;

  @Override
  public MassPoint getInitialKinematicModel() {
    return MassPoint.newBuilder().x(x).y(y).vx(vx).vy(vy).ax(x).ay(y).mass(mass).build();
  }

  @Override
  public Circle getShape() {
    return Circle.newBuilder().appearance(appearance).radius(radius).build();
  }

  @Override
  public Painter getPainter() {
    return assembly.getPainterFactory().getCirclePainter();
  }
}
