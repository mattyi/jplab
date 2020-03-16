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

  public static class Builder {

    private HashMap<String, KinematicModel> kinematicModels;
    private HashSet<String> updatedFields;

    public Builder() {
      kinematicModels = new HashMap<>();
    }

    public Builder kinematicModel(String name, KinematicModel model) {
      kinematicModels.put(name, model);
      return this;
    }

    public Builder set(String name, KinematicModel model) {
      return kinematicModel(name, model);
    }

    public Builder update(String name, KinematicModel model) {
      if (kinematicModels.containsKey(name)) {
        kinematicModels.put(name, model);
        updatedFields.add(name);
      }
      return this;
    }

    public KinematicModel get(String name) {
      return kinematicModels.get(name);
    }

    public boolean isUpdated(String name) {
      return updatedFields.contains(name);
    }

    public AssemblySnapshot build() {
      return new AssemblySnapshot(this);
    }
  }
}
