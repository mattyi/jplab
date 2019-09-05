package com.hzyi.jplab.core.model;


public interface Component {

  String getName();

  double x();

  double y();

  double theta();

  double vx();

  double vy();

  double omega();

  double ax();

  double ay();

  double alpha();

  Component update(ComponentState componentState);

  ComponentState getInitialComponentState();

}