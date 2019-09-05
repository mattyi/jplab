package com.hzyi.jplab.core.model;


public abstract class StaticComponent implements Component {

  @Override
  public final double vx() {
    return 0;
  }

  @Override
  public final double vy() {
    return 0;
  }

  @Override
  public final double omega() {
    return 0;
  }

  @Override
  public final double ax() {
    return 0;
  }

  @Override
  public final double ay() {
    return 0;
  }

  @Override
  public final double alpha() {
    return 0;
  }
}