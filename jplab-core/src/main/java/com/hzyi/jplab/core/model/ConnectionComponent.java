package com.hzyi.jplab.core.model;

import com.hzyi.jplab.core.util.Coordinate;

public class ConnectionComponent extends Component {

  private Component end1;
  private Component end2;
  private Coordinate connectionPoint1;
  private Coordinate connectionPoint2;

  ConnectionComponent(Builder<?> builder) {
    super(builder);
    this.end1 = builder.end1;
    this.end2 = builder.end2;
    this.connectionPoint1 = builder.connectionPoint1;
    this.connectionPoint2 = builder.connectionPoint2;
  }

  public Builder newBuilder() {
    return new Builder();
  }

  public static class Builder<T extends Builder<T>> extends Component.Builder<T> {
  
    private Component end1;
    private Component end2;
    private Coordinate connectionPoint1;
    private Coordinate connectionPoint2;

    protected Builder(){}
 
    @SuppressWarnings("Unchecked")
    public T setEnd1(Component end1) {
      this.end1 = end1;
      return (T)this;
    }

    @SuppressWarnings("Unchecked")
    public T setEnd2(Component end2) {
      this.end2 = end2;
      return (T)this;
    }

    @SuppressWarnings("Unchecked")
    public T setConnectionPoint1(Coordinate connectionPoint1) {
      this.connectionPoint1 = connectionPoint1;
      return (T)this;
    }

    @SuppressWarnings("Unchecked")
    public T setConnectionPoint2(Coordinate connectionPoint2) {
      this.connectionPoint2 = connectionPoint2;
      return (T)this;
    }

    public ConnectionComponent build() {
      return new ConnectionComponent(this);
    }

  }
  

}