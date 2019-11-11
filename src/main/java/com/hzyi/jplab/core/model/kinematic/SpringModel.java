package com.hzyi.jplab.core.model.kinematic;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Builder(builderMethodName = "newBuilder", toBuilder = true)
public class SpringModel extends ConnectingModel {

  @Getter private double stiffness;
  @Getter private double originalLength;
  @Getter private double connectingPointAX;
  @Getter private double connectingPointAY;
  @Getter private double connectingPointBX;
  @Getter private double connectingPointBY;
  @Getter private KinematicModel connectingModelA;
  @Getter private KinematicModel connectingModelB;
}
