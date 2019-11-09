package com.hzyi.jplab.core.model;

import com.hzyi.jplab.core.model.kinematic.StaticModel;
import com.hzyi.jplab.core.model.shape.Appearance;
import com.hzyi.jplab.core.model.shape.Edge;
import com.hzyi.jplab.core.painter.Painter;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder(builderMethodName = "newBuilder")
public class Wall implements Component {

  private double x;
  private double y;
  private double theta;
  @Getter private String name;

  private double innerLineHeight;
  private double innerLineAngle;
  private double innerLineCount;
  private double length;

  private Appearance appearance;
  @Getter @Setter private Assembly assembly;

  @Override
  public Painter getPainter() {
    return assembly.getPainterFactory().getEdgePainter();
  }

  @Override
  public StaticModel getInitialKinematicModel() {
    return StaticModel.newBuilder().x(x).y(y).theta(theta).build();
  }

  @Override
  public Edge getShape() {
    return Edge.newBuilder()
        .appearance(appearance)
        .innerLineHeight(innerLineHeight)
        .innerLineAngle(innerLineAngle)
        .innerLineCount(innerLineCount)
        .length(length)
        .build();
  }
}
