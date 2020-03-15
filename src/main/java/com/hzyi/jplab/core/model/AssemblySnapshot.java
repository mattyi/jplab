package com.hzyi.jplab.core.model;

import com.google.common.collect.ImmutableMap;
import com.hzyi.jplab.core.model.kinematic.KinematicModel;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class AssemblySnapshot {

  private final Map<String, KinematicModel> kinematicModels;

  private AssemblySnapshot(Builder builder) {
    kinematicModels = ImmutableMap.copyOf(builder.kinematicModels);
  }

  public KinematicModel get(String name) {
    return kinematicModels.get(name);
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public Builder toBuilder() {
    Builder builder = new Builder();
    for (Map.Entry<String, KinematicModel> entry : kinematicModels.entrySet()) {
      builder.kinematicModel(entry.getKey(), entry.getValue());
    }
    return builder;
  }

  public AssmeblySnapshot advanceTimeStep(double timeStep) {
    Builder builder = new Builder();
    for (Map.Entry<String, KinematicModel> entry : kinematicModels.entrySet()) {
      KinematicModel model = entry.getValue();
      if (model instanceof ConnectingModel) {

      } else {

      }
    }
    return builder.build();
  }

  public static class Builder {

    private HashMap<String, KinematicModel> kinematicModels;

    public Builder kinematicModel(String name, KinematicModel model) {
      kinematicModels.put(name, model);
      return this;
    }

    public Builder set(String name, KinematicModel model) {
      return kinematicModel(name, model);
    }

    public KinematicModel get(String name) {
      return kinematicModels.get(name);
    }

    public AssemblySnapshot build() {
      return new AssemblySnapshot(this);
    }
  }
}
