package com.hzyi.jplab.core.model;

public abstract class MassPoint implements Component {

  public double mass();

  @Override
  public final double theta() {
    return 0;
  }

  @Override
  public final double omega() {
    return 0;
  }

  @Override
  public final double alpha() {
    return 0;
  }

}