package com.hzyi.jplab.model.component;

import static com.google.common.base.Preconditions.checkArgument;

class MassPoint extends DynamicComponent {

  protected double mass;
  protected double momentOfInertia;

  MassPoint(Builder<?> builder) {
    super(builder);
    this.initState = newComponentStateBuilder(builder).build();
    this.mass = builder.mass;
    this.momentOfInertia = builder.momentOfInertia;
  }

  public double getMass() {
    return this.mass;
  }

  public double getMomentOfInertia() {
    return this.momentOfInertia;
  }

  protected ComponentState.Builder newComponentStateBuilder(Builder<?> builder) {
    return super.newComponentStateBuilder(builder);
  }

  public static class Builder<T extends Builder<T>>
      extends DynamicComponent.Builder<T> {

    protected double mass, momentOfInertia;

    @SuppressWarnings("Unchecked")
    public T setMass(double mass) {
      this.mass = mass;
      return (T)this;
    }

    @SuppressWarnings("Unchecked")
    public T setMomentOfInertia(double momentOfInertia) {
      this.momentOfInertia = momentOfInertia;
      return (T)this;
    }

    @Override
    public MassPoint build() {
      checkArgument(mass > 0, "Mass has to be positive.");
      checkArgument(momentOfInertia > 0, "Moment of Inertia has to be positive.");
      return new MassPoint(this);
    }
  }
}