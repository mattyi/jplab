package com.hzyi.jplab.core.util;

import static com.google.common.truth.Truth.assertThat;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@RunWith(JUnit4.class)
public class CoordinateTest {
  
  @Test
  public void testNewCoordinate() {
    Coordinate c = new Coordinate(1.0, 2.0);
  }

  @Test
  public void testCoordinateSetXY() {
    Coordinate c = new Coordinate(1.0, 2.0);
    c.x(3.0).y(4.0);
    assertThat(c.x()).isEqualTo(3.0);
    assertThat(c.y()).isEqualTo(4.0); 
  }
}