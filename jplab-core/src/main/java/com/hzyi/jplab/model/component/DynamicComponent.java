package com.hzyi.jplab.model.component;

public class DynamicComponent extends StaticComponent {
     
  private static final Field V_X = Field.addField("vx");
  private static final Field V_Y = Field.addField("vy");
  private static final Field OMEGA = Field.addField("omega");

  public static Field V_X() {
    return V_X;
  }

  public static Field V_Y() {
    return V_Y;
  }

  public static Field OMEGA() {
    return OMEGA;
  }

  DynamicComponent(Builder<?> builder) {
    super(builder);
    this.initState = newComponentStateBuilder(builder).build();
  }

  protected ComponentState.Builder newComponentStateBuilder(Builder<?> builder) {
    ComponentState.Builder superBuilder = super.newComponentStateBuilder(builder);
    return superBuilder
        .set(V_X, builder.vx)
        .set(V_Y, builder.vy)
        .set(OMEGA, builder.omega);
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