package com.hzyi.jplab.model.component;

import static com.google.common.base.Preconditions.checkNotNull;

class StaticComponent extends Component {
     
  private static final Field LOC_X = Field.addField("locx");
  private static final Field LOC_Y = Field.addField("locy");
  private static final Field DIR_X = Field.addField("dirx");
  private static final Field DIR_Y = Field.addField("diry");
  
  static Field locX() {
    return LOC_X;
  }

  static Field locY() {
    return LOC_Y;
  }

  static Field dirX() {
    return DIR_X;
  }

  static Field dirY() {
    return DIR_Y;
  }

  private final ComponentState initState;

  StaticComponent(Builder<?> builder) {
    super(builder);
    this.initState = ComponentState
        .newBuilder(this)
        .set(LOC_X, builder.x)
        .set(LOC_Y, builder.y)
        .set(DIR_X, builder.dx)
        .set(DIR_Y, builder.dy)
        .build();
  }

  public static class Builder<T extends Builder<T>>
      extends Component.Builder<T> {

    private double x, y, dx, dy;

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
      checkNotNull(x);
      checkNotNull(y);
      checkNotNull(dx);
      checkNotNull(dy);
      return new StaticComponent(this);
    }
  }
}