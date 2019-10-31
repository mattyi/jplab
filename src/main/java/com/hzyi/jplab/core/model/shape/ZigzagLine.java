package com.hzyi.jplab.core.model.shape;

import lombok.Getter;

public class ZigzagLine extends Shape {

  @Getter(fluent = true)
  private double length;

  @Getter(fluent = true)
  private double width;

  @Getter(fluent = true)
  private int zigzagCount;

  @Getter private Appearance appearance;
}
