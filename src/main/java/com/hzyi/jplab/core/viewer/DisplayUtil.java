package com.hzyi.jplab.core.viewer;

import javafx.scene.canvas.GraphicsContext;

public class DisplayUtil {

  private DisplayUtil() {
    // util class
  }

  public static javafx.scene.paint.Color toJavaFxColor (Appearance.Color color) {
    switch (color) {
      case RED:
          return javafx.scene.paint.Color.RED;
      case BLUE:
          return javafx.scene.paint.Color.BLUE;
      case YELLOW:
          return javafx.scene.paint.Color.YELLOW;
      case GREEN:
          return javafx.scene.paint.Color.GREEN;
      case BLACK:
          return javafx.scene.paint.Color.BLACK;
      case WHITE:
          return javafx.scene.paint.Color.WHITE;
      case GRAY:
          return javafx.scene.paint.Color.GRAY;
      default:
          throw new IllegalArgumentException("Unknown color: " + color);
    }
  }

  public static void graphicsContextColorAndStyle(GraphicsContext graphics, Appearance appearance) {
    graphics.setFill(toJavaFxColor(appearance.getColor()));
    graphics.setStroke(toJavaFxColor(appearance.getColor()));
    graphics.setLineWidth(appearance.getLineWidth());
  }
}