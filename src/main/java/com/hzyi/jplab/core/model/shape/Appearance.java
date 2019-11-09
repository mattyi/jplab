package com.hzyi.jplab.core.model.shape;

import lombok.Builder;
import lombok.Getter;

@Builder(builderMethodName = "newBuilder")
public class Appearance {

  public static enum Color {
    RED,
    BLUE,
    YELLOW,
    GREEN,
    BLACK,
    WHITE,
    GRAY
  }

  public static enum Style {
    FILL,
    STROKE
  }

  @Getter private Color color;
  @Getter private Style style;
  @Getter private double lineWidth;

  public static Appearance of() {
    return newBuilder().color(Color.RED).style(Style.FILL).lineWidth(1).build();
  }
}
