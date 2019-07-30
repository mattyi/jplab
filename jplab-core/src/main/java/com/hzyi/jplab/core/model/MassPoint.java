package com.hzyi.jplab.core.model;

import static com.google.common.base.Preconditions.checkArgument;

class abstract MassPoint extends DynamicComponent {

  protected double mass;
  protected double momentOfInertia;

  public double getMass() {
    return this.mass;
  }

  public double getMomentOfInertia() {
    return this.momentOfInertia;
  }

  protected ComponentState.Builder newComponentStateBuilder(Builder<?> builder) {
    return super.newComponentStateBuilder(builder);
  }
}