package com.hzyi.jplab.core.viewer;

@AutoValue
public abstract class DisplayContext implements Buildable {

  public static enum Color {
    red, blue, yellow, green, black, white
  }

  public static enum Style {
    fill, stroke
  }

  public abstract Color getColor();
  
  public abstract Style getStyle();

  public newBuilder() {

  }

  @AutoValue.Builder
  public static class Builder {

    public abstract Builder setColor(Color color);

    public abstract Builder setStyle(Style style);

    public abstract DisplayContext build();
  }

}