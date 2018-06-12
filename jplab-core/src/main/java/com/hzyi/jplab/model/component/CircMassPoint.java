package com.hzyi.jplab.model.component;

import static com.google.common.base.Preconditions.checkArgument;

public final class CircMassPoint extends MassPoint {

  private final double radius;

  private CircMassPoint(Builder builder) {
    super(builder);
    this.radius = builder.radius;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public double getRadius() {
    return this.radius;
  }

  public static final class Builder
      extends MassPoint.Builder<Builder> {
    
    protected double radius;

    public Builder setRadius(double radius) {
      this.radius = radius;
      return this;
    }
 
    @Override
    public CircMassPoint build() {
      checkArgument(radius > 0, "radius has to be positive");
      return new CircMassPoint(this);
    }
  }
}