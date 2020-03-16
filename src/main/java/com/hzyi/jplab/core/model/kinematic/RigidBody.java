package com.hzyi.jplab.core.model.kinematic;

public abstract class RigidBody extends SingleKinematicModel {

  public abstract double mass();

  public abstract double momentOfInertia();
}
