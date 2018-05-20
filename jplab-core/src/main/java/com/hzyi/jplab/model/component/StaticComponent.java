package com.hzyi.jplab.model.component;

class StaticComponent extends Component {
     
  private static final Field LOC_X = Field.addField("locx");
  private static final Field LOC_Y = Field.addField("locy");
  private static final Field DIR_X = Field.addField("dirx");
  private static final Field DIR_Y = Field.addField("diry");
  
  static Field LOC_X() {
    return LOC_X;
  }

  static Field LOC_Y() {
    return LOC_Y;
  }

  static Field DIR_X() {
    return DIR_X;
  }

  static Field DIR_Y() {
    return DIR_Y;
  }

  protected ComponentState initState;

  StaticComponent(Builder<?> builder) {
    super(builder);
    this.initState = newComponentStateBuilder(builder).build();
  }

  public ComponentState getInitialComponentState() {
    return initState;
  }

  protected ComponentState.Builder newComponentStateBuilder(Builder<?> builder) {
    return ComponentState.newBuilder()
        .setComponent(this)
        .set(LOC_X, builder.x)
        .set(LOC_Y, builder.y)
        .set(DIR_X, builder.dx)
        .set(DIR_Y, builder.dy);
  }

  public static class Builder<T extends Builder<T>>
      extends Component.Builder<T> {

    protected double x, y, dx, dy;

    @SuppressWarnings("Unchecked")
    T setX(double x) {
      this.x = x;
      return (T)this;
    }

    @SuppressWarnings("Unchecked")
    T setY(double y) {
      this.y = y;
      return (T)this;
    }

    @SuppressWarnings("Unchecked")
    T setDirX(double dx) {
      this.dx = dx;
      return (T)this;
    }

    @SuppressWarnings("Unchecked")
    T setDirY(double dy) {
      this.dy = dy;
      return (T)this;
    }

    @Override
    public StaticComponent build() {
      return new StaticComponent(this);
    }
  }
}