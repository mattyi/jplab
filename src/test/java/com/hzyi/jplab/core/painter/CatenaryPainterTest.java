package com.hzyi.jplab.core.painter;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableList;
import com.hzyi.jplab.core.util.Coordinate;
import java.util.List;
import lombok.AllArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
@AllArgsConstructor
public class CatenaryPainterTest {

  private final String name;
  private final Coordinate left;
  private final Coordinate right;
  private final double length;

  @Parameters
  public static List<Object[]> data() {
    return ImmutableList.of(
        new Object[] {"in-between", Coordinate.of(-1.0, 1.0), Coordinate.of(1.0, 1.0), 3.0},
        new Object[] {"in-between-left-lower", Coordinate.of(-1.0, 0.5), Coordinate.of(1, 1), 3.0},
        new Object[] {"in-between-right-lower", Coordinate.of(-1.0, 1.5), Coordinate.of(1, 1), 3.0},
        new Object[] {"in-between-moved-x", Coordinate.of(0.0, 1.5), Coordinate.of(1, 1), 3.0},
        new Object[] {"to-the-left", Coordinate.of(1.0, 1.0), Coordinate.of(2.0, 2.0), 1.5},
        new Object[] {"to-the-right", Coordinate.of(1.0, 2.0), Coordinate.of(2.0, 1.0), 1.5},
        new Object[] {"larger-scale", Coordinate.of(0.0, 100.0), Coordinate.of(80.0, 0.0), 300.0});
  }

  @Test
  public void test() {
    double a = CatenaryPainter.a(left, right, length, name);
    Coordinate o0 = CatenaryPainter.o0(left, right, name, a);
    assertThat(a).isNotEqualTo(Double.NaN);
    assertThat(o0.x()).isNotEqualTo(Double.NaN);
    assertThat(o0.y()).isNotEqualTo(Double.NaN);
  }
}
