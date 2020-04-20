package com.hzyi.jplab.core.model;

import com.hzyi.jplab.core.application.Application;
import com.hzyi.jplab.core.model.kinematic.SingleKinematicModel;
import com.hzyi.jplab.core.model.kinematic.SpringModel;
import com.hzyi.jplab.core.model.shape.Appearance;
import com.hzyi.jplab.core.model.shape.Shape;
import com.hzyi.jplab.core.model.shape.ZigzagLine;
import com.hzyi.jplab.core.painter.Painter;
import lombok.Builder;
import lombok.Getter;

@Builder(builderMethodName = "newBuilder")
public class Spring implements Component {

  @Getter private String name;
  private double stiffness;
  private double originalLength;
  private Component<? extends SingleKinematicModel, ? extends Shape> componentA;
  private Component<? extends SingleKinematicModel, ? extends Shape> componentB;
  private double connectingPointAX;
  private double connectingPointAY;
  private double connectingPointBX;
  private double connectingPointBY;
  private double width;
  private int zigzagCount;
  private Appearance appearance;

  @Override
  public SpringModel getInitialKinematicModel() {
    return SpringModel.newBuilder()
        .name(name)
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
    return Application.getPainterFactory().getZigzagLinePainter();
  }
}
