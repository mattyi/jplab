package com.hzyi.jplab.core.model;

public abstract class MassPoint implements RigidBody {

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

  @Override
  public final double momentOfInertia() {
    return 0;
  }

}