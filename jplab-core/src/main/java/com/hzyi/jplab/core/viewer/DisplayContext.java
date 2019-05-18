package com.hzyi.jplab.core.viewer;

import com.google.auto.value.AutoValue;


@AutoValue
public abstract class DisplayContext {

  public static enum Color {
    red, blue, yellow, green, black, white, gray
  }

  public static enum Style {
    fill, stroke
  }

  public abstract Color color();
  
  public abstract Style style();

  public static DisplayContext of() {
    return newBuilder().color(Color.red).style(Style.fill).build();
  }

  public static Builder newBuilder() {
    return new AutoValue_DisplayContext.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder color(Color color);

    public abstract Builder style(Style style);

    public abstract DisplayContext build();
  }

}