package com.hzyi.jplab.core.model;

import com.hzyi.jplab.core.util.Coordinate;

public class Spring extends ConnectionComponent {

  private double k;
  private double origLength;

  Spring(Builder builder) {
    super(builder);
    this.k = builder.k;
    this.origLength = builder.origLength;
  }

  public double getK() {
    return k;
  }

  public double getOrigLength() {
    return origLength;
  }

  public static final class Builder extends ConnectionComponent.Builder<Builder> {

    private double k;
    private double origLength;

    protected Builder(){}

    public Builder setK(double k) {
      this.k = k;
      return this;
    }

    public Builder setOrigLength(double origLength) {
      this.origLength = origLength;
      return this;
    }

    @Override
    public Spring build() {
      return new Spring(this);
    }

  }





}