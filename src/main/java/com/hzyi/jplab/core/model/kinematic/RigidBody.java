package com.hzyi.jplab.core.model.kinematic;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import java.util.List;

public abstract class RigidBody extends SingleKinematicModel {

  public abstract double mass();

  public abstract double momentOfInertia();

  @Override
  public List<String> codependentFields() {
    return ImmutableList.of(
        getFieldFullName("ax"), getFieldFullName("ay"), getFieldFullName("omega"));
  }

  @Override
  public Table<String, String, Double> codependentMultipliers() {
    return ImmutableTable.<String, String, Double>builder()
        .put(getFieldFullName("ax"), getFieldFullName("ax"), -mass())
        .put(getFieldFullName("ay"), getFieldFullName("ay"), -mass())
        .put(getFieldFullName("omega"), getFieldFullName("omega"), -momentOfInertia())
        .build();
  }
}
