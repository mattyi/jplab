package com.hzyi.jplab.core.model;

public class DynamicComponent extends StaticComponent {

  DynamicComponent(Builder<?> builder) {
    super(builder);
    this.initState = newComponentStateBuilder(builder).build();
  }

  protected ComponentState.Builder newComponentStateBuilder(Builder<?> builder) {
    ComponentState.Builder superBuilder = super.newComponentStateBuilder(builder);
    return superBuilder
        .set(Field.VX, builder.vx)
        .set(Field.VY, builder.vy)
        .set(Field.OMEGA, builder.omega);
  }

  public static class Builder<T extends Builder<T>>
      extends StaticComponent.Builder<T> {

    protected double vx, vy, omega;

    @SuppressWarnings("Unchecked")
    public T setVX(double vx) {
      this.vx = vx;
      return (T)this;
    }

    @SuppressWarnings("Unchecked")
    public T setVY(double vy) {
      this.vy = vy;
      return (T)this;
    }

    @SuppressWarnings("Unchecked")
    public T setOmega(double omega) {
      this.omega = omega;
      return (T)this;
    }

    @Override
    public DynamicComponent build() {
      return new DynamicComponent(this);
    }
  }
}