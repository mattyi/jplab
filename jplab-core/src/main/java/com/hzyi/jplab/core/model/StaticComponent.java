package com.hzyi.jplab.core.model;

class StaticComponent extends Component {
     
  StaticComponent(Builder<?> builder) {
    super(builder);
    this.initState = newComponentStateBuilder(builder).build();
  }

  public ComponentState getInitialComponentState() {
    return initState;
  }

  protected ComponentState.Builder newComponentStateBuilder(Builder<?> builder) {
    return super.newComponentStateBuilder(builder)
        .set(Field.LOCX, builder.x)
        .set(Field.LOCY, builder.y)
        .set(Field.DIRX, builder.dx)
        .set(Field.DIRY, builder.dy);
  }

  public static class Builder<T extends Builder<T>>
      extends Component.Builder<T> {

    protected double x, y, dx, dy;

    @SuppressWarnings("Unchecked")
    public T setX(double x) {
      this.x = x;
      return (T)this;
    }

    @SuppressWarnings("Unchecked")
    public T setY(double y) {
      this.y = y;
      return (T)this;
    }

    @SuppressWarnings("Unchecked")
    public T setDirX(double dx) {
      this.dx = dx;
      return (T)this;
    }

    @SuppressWarnings("Unchecked")
    public T setDirY(double dy) {
      this.dy = dy;
      return (T)this;
    }

    @Override
    public StaticComponent build() {
      return new StaticComponent(this);
    }
  }
}