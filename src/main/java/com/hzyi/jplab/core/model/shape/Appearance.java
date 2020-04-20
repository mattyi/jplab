package com.hzyi.jplab.core.model.shape;

import java.util.Map;
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

  public static AppearanceBuilder newDefaultBuilder() {
    return newBuilder().color(Color.RED).style(Style.FILL).lineWidth(1);
  }

  public static Appearance unpack(Map<String, ?> map) {
    AppearanceBuilder builder = newDefaultBuilder();
    if (map.containsKey("color")) {
      builder.color(Color.valueOf(((String) map.get("color")).toUpperCase()));
    }

    if (map.containsKey("style")) {
      builder.style(Style.valueOf(((String) map.get("style")).toUpperCase()));
    }

    if (map.containsKey("line_width")) {
      builder.lineWidth((Double) map.get("line_width"));
    }

    return builder.build();
  }
}
