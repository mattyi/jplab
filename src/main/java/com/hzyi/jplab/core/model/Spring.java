package com.hzyi.jplab.core.model;

import com.hzyi.jplab.core.painter.Appearance;
import com.hzyi.jplab.core.painter.Painter;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder(builderMethodName = "newBuilder")
public class Spring implements Component {

  @Getter private String name;
  private double stiffness;
  private Component componentA;
  private Component componentB;
  private double connectingPointAX;
  private double connectingPointAY;
  private double connectingPointBX;
  private double connectingPointBY;

  @Getter @Setter private Assembly assembly;

  private double width;
  private int zigzagCount;
  private Appearance appearance;

  @Override
  public KinematicModel getInitialKinematicModel() {
    return MassPoint.newBuilder()
        .stiffness(stiffness)
        .componentA(componentA)
        .componentB(componentB)
        .componentAX(componentAX)
        .componentAY(componentAY)
        .componentBX(componentBX)
        .componentBY(componentBY)
        .build();
  }

  @Override
  public Shape getShape() {
    return ZigzagLine.newBuilder()
        .appearance(appearance)
        .zigzagCount(zigzagCount)
        .width(width)
        .build();
  }

  @Override
  public Painter getPainter() {
    return assembly.getPainterFactory().getZigzagLinePainter();
  }
}
