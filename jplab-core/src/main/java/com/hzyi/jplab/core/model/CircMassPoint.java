package com.hzyi.jplab.core.model;

import static com.google.common.base.Preconditions.checkArgument;
import java.util.function.BiFunction;

public final class CircMassPoint extends MassPoint {

  public static final BiFunction<Component, ComponentState, double[]> TO_CIRCLE_PAINTER_PARAMS =
      (c, s) -> {
        checkArgument(c instanceof CircMassPoint);
        CircMassPoint p = (CircMassPoint)c;
        return new double[]{s.get(Field.LOCX), s.get(Field.LOCY), p.getRadius()};
      };

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