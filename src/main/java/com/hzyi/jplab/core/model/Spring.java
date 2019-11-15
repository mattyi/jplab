package com.hzyi.jplab.core.model;

import com.hzyi.jplab.core.model.kinematic.SpringModel;
import com.hzyi.jplab.core.model.shape.Appearance;
import com.hzyi.jplab.core.model.shape.ZigzagLine;
import com.hzyi.jplab.core.painter.Painter;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder(builderMethodName = "newBuilder")
public class Spring implements Component {

  @Getter private String name;
  private double stiffness;
  private double originalLength;
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
  public SpringModel getInitialKinematicModel() {
    return SpringModel.newBuilder()
        .stiffness(stiffness)
        .originalLength(originalLength)
        .connectingModelA(componentA.getInitialKinematicModel())
        .connectingModelB(componentB.getInitialKinematicModel())
        .relativeConnectingPointAX(connectingPointAX)
        .relativeConnectingPointAY(connectingPointAY)
        .relativeConnectingPointBX(connectingPointBX)
        .relativeConnectingPointBY(connectingPointBY)
        .build();
  }

  @Override
  public ZigzagLine getShape() {
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
