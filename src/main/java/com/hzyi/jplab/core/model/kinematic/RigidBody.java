package com.hzyi.jplab.core.model.kinematic;

public interface RigidBody extends KinematicModel {

  public double mass();

  public double momentOfInertia();
}
