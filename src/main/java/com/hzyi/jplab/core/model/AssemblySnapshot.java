package com.hzyi.jplab.core.model;

import com.hzyi.jplab.core.model.kinematic.KinematicModel;
import java.util.Map;
import lombok.Builder;
import lombok.Singular;

@Builder(builderMethodName = "newBuilder")
public class AssemblySnapshot {

  @Singular private final Map<String, KinematicModel> kinematicModels;

  public KinematicModel get(String name) {
    return kinematicModels.get(name);
  }
}
