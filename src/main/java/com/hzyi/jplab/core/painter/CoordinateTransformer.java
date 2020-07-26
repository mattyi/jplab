package com.hzyi.jplab.core.painter;

import com.hzyi.jplab.core.util.Coordinate;
import com.hzyi.jplab.core.util.CoordinateSystem;
import com.hzyi.jplab.core.util.Coordinates;
import javafx.scene.canvas.Canvas;

/**
 * CoordianteTransformer manages the transformation of coordinates and scalars between different
 * coordinate systems.
 */
public class CoordinateTransformer {

  private final Canvas canvas;
  private final double naturalZoomScale;
  private final CoordinateSystem natural = new CoordinateSystem(0, 0, 1, 0, 0, 1);
  private final CoordinateSystem absolute = new CoordinateSystem(0, 0, 1, 0, 0, 1);
  private final CoordinateSystem screen = new CoordinateSystem(0, 0, 1, 0, 0, -1);

  public CoordinateTransformer(Canvas canvas, double naturalZoomScale) {
    this.canvas = canvas;
    this.naturalZoomScale = naturalZoomScale;
    canvas
        .widthProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              screen.origin().x(((double) newVal) / -2.0);
            });
    canvas
        .heightProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              screen.origin().y(((double) newVal) / 2.0);
            });
  }

  // For testing purpose.
  public static CoordinateTransformer getTestingCoordinateTransformer() {
    Canvas canvas = new Canvas(1, 1);
    return new CoordinateTransformer(canvas, 1.0);
  }

  /**
   * Returns the current natural coordinate system relative to the absolute coordinate system. The
   * origin is located at the center of the canvas. The positive direction of the X axis is right
   * and the positive direction of the Y axis is up. The unit lengths of both axes are fixed,
   * independent of current size of the canvas, and are always set to naturalZoomScale times those
   * of the absolute coorinate system.
   */
  public CoordinateSystem natural() {
    return natural.ux(naturalZoomScale, 0).uy(0, naturalZoomScale);
  }

  /**
   * Returns the current absolute coordinate system. The origin is located at the center of the
   * canvas. The positive direction of the X axis is right and the positive direction of the Y axis
   * is up. The unit lengths of both axes are the lengths of one pixel.
   */
  public CoordinateSystem absolute() {
    return absolute;
  }

  /**
   * Returns the current screen coordinate system. The origin is located top left corner of the
   * canvas. The positive direction of the X axis is right and the positive direction of the Y axis
   * is down. The unit lengths of both axes are the same as those of the absolute coordinate system.
   */
  public CoordinateSystem screen() {
    screen.origin(-canvas.getWidth() / 2, canvas.getHeight() / 2);
    return screen;
  }

  /**
   * Returns the coordinate of a vector in the screen coordinate system given its coordinate in the
   * natual coordinate sytem.
   */
  public Coordinate toScreen(Coordinate natural) {
    return Coordinates.transform(natural, natural(), screen());
  }

  /**
   * Returns the coordinate a vector in the natural coordinate system given its coordinate in the
   * screen coordinate sytem.
   */
  public Coordinate toNatural(Coordinate screen) {
    return Coordinates.transform(screen, screen(), natural());
  }

  /** Returns the body coordinate system relative to the absolute coordinate system. */
  public CoordinateSystem getBodyCoordinateSystem(double x, double y) {
    return new CoordinateSystem(
        x * naturalZoomScale, y * naturalZoomScale, naturalZoomScale, 0, 0, naturalZoomScale);
  }

  /**
   * Returns the length of a vector in the screen coordinate system given its length in the natual
   * coordinate sytem.
   */
  public double toScreen(double natural) {
    return natural * naturalZoomScale;
  }

  /**
   * Returns the length of a vector in the natural coordinate system given its length in the screen
   * coordinate sytem.
   */
  public double toNatural(double screen) {
    return screen / naturalZoomScale;
  }
}
