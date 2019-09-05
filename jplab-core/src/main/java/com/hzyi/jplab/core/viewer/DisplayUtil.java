package com.hzyi.jplab.core.viewer;

import javafx.scene.canvas.GraphicsContext;

public class DisplayUtil {

  private DisplayUtil() {
    // util class
  }

  public static javafx.scene.paint.Color toJavaFxColor (Appearance.Color color) {
    switch (color) {
      case red:
          return javafx.scene.paint.Color.RED;
      case blue:
          return javafx.scene.paint.Color.BLUE;
      case yellow:
          return javafx.scene.paint.Color.YELLOW;
      case green:
          return javafx.scene.paint.Color.GREEN;
      case black:
          return javafx.scene.paint.Color.BLACK;
      case white:
          return javafx.scene.paint.Color.WHITE;
      case gray:
          return javafx.scene.paint.Color.GRAY;
      default:
          throw new IllegalArgumentException("Unknown color: " + color);
    }
  }

  public static void graphicsContextColorAndStyle(GraphicsContext graphics, Appearance context) {
    switch (context.style()) {
      case fill:
          graphics.setFill(toJavaFxColor(context.color()));
          break;
      case stroke:
          graphics.setStroke(toJavaFxColor(context.color()));
          break;
      default:
          throw new IllegalArgumentException("Unknown style: " + context.style());
    }
  }


}