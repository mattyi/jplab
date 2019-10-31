package com.hzyi.jplab.core.model.kinematic;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessor;

@Accessor(fluent = true)
@Builder(builderMethodName = "newBuilder")
public class SpringModel extends ConnetingModel {

  @Getter private double stiffness;
  @Getter private double connectingComponentAX;
  @Getter private double connectingComponentAY;
  @Getter private double connectingComponentBX;
  @Getter private double connectingComponentBY;
  @Getter private Component connectingComponentA;
  @Getter private Component connectingComponentB;
}
