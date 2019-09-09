package com.hzyi.jplab.core.util;

import static com.google.common.truth.Truth.assertThat;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@RunWith(JUnit4.class)
public class CoordinatesTest {
  
  @Test
  public void testTransform() {
    CoordinateSystem cs1 = new CoordinateSystem(0, 0, 1, 0, 0, 1);
    Coordinate c1 = new Coordinate(1, 1);
    CoordinateSystem cs2 = new CoordinateSystem(3, 3, -1, 0, 0, 1);
    Coordinate c2 = Coordinates.transform(c1, cs1, cs2);
    assertThat(Coordinates.areEqual(new Coordinate(2, -2), c2)).isTrue();
  }

  @Test
  public void testTransformScale() {
    CoordinateSystem cs1 = new CoordinateSystem(0, 0, 1, 0, 0, 1);
    Coordinate c1 = new Coordinate(1, 1);
    CoordinateSystem cs2 = new CoordinateSystem(0, 0, 3, 0, 0, 3);
    Coordinate c2 = Coordinates.transform(c1, cs1, cs2);
    assertThat(Coordinates.areEqual(new Coordinate(1.0/3, 1.0/3), c2)).isTrue();
  }

}