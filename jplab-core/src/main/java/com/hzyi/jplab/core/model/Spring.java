package com.hzyi.jplab.core.model;

import com.hzyi.jplab.core.util.Coordinate;

public class Spring implements ConnectionComponent, Line {

  @Getter private double stiffness;
  @Getter private Component componentA;
  @Getter private Component componentB; 

}