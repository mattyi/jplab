package com.hzyi.jplab.core.util;

import static com.google.common.truth.Truth.assertThat;
import static com.hzyi.jplab.core.model.Constraint.cof;
import static com.hzyi.jplab.core.model.Property.constant;
import static com.hzyi.jplab.core.model.Property.pof;
import static org.junit.Assert.fail;

import com.google.common.collect.ImmutableList;
import com.hzyi.jplab.core.model.Constraint;
import com.hzyi.jplab.core.model.Property;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class DictionaryMatrixTest {

  @Test
  public void testCreate() {
    List<Constraint> cons = ImmutableList.of(cof("a.first"), cof("a.second"));
    List<Property> props = ImmutableList.of(pof("a.first"), pof("a.second"), constant());
    DictionaryMatrix matrix = new DictionaryMatrix(cons, props);
    assertThat(matrix.getRow(cof("a.first")))
        .containsExactly(pof("a.first"), 0.0, pof("a.second"), 0.0, constant(), 0.0);
  }

  @Test
  public void testCreateFailDupKeys() {
    try {
      List<Constraint> cons = ImmutableList.of(cof("a.first"), cof("a.first"));
      List<Property> props = ImmutableList.of(pof("a.first"), pof("a.second"), constant());
      DictionaryMatrix matrix = new DictionaryMatrix(cons, props);
      fail();
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage()).isEqualTo("non-unique constraint: a.first");
    }
  }

  @Test
  public void testSet() {
    List<Constraint> cons = ImmutableList.of(cof("a.first"), cof("a.second"));
    List<Property> props = ImmutableList.of(pof("a.first"), pof("a.second"), constant());
    DictionaryMatrix matrix = new DictionaryMatrix(cons, props);
    matrix.set(cof("a.first"), pof("a.first"), 2.0);
    assertThat(matrix.get(cof("a.first"), pof("a.first"))).isEqualTo(2.0);
  }

  @Test
  public void testAdd() {
    List<Constraint> cons = ImmutableList.of(cof("a.first"), cof("a.second"));
    List<Property> props = ImmutableList.of(pof("a.first"), pof("a.second"), constant());
    DictionaryMatrix matrix = new DictionaryMatrix(cons, props);
    matrix.add(cof("a.first"), pof("a.second"), 2.0);
    assertThat(matrix.get(cof("a.first"), pof("a.second"))).isEqualTo(2.0);
    matrix.add(cof("a.first"), pof("a.second"), 2.0);
    assertThat(matrix.get(cof("a.first"), pof("a.second"))).isEqualTo(4.0);
  }

  @Test
  public void testSetAndAddFail() {
    List<Constraint> cons = ImmutableList.of(cof("a.first"), cof("a.second"));
    List<Property> props = ImmutableList.of(pof("a.first"), pof("a.second"), constant());
    DictionaryMatrix matrix = new DictionaryMatrix(cons, props);
    try {
      matrix.add(cof("a.not_first"), pof("a.second"), 2.0);
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage()).isEqualTo("non-existing constraint: a.not_first");
    }

    try {
      matrix.set(cof("a.first"), pof("a.not_second"), 2.0);
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage()).isEqualTo("non-existing property: a.not_second");
    }
  }

  @Test
  public void testGetRowAndCol() {
    List<Constraint> cons = ImmutableList.of(cof("a.first"), cof("a.second"));
    List<Property> props = ImmutableList.of(pof("a.first"), pof("a.second"), constant());
    DictionaryMatrix matrix = new DictionaryMatrix(cons, props);
    matrix.set(cof("a.first"), pof("a.first"), 1.0);
    matrix.set(cof("a.first"), pof("a.second"), 2.0);
    assertThat(matrix.getRow(cof("a.first")))
        .containsExactly(pof("a.first"), 1.0, pof("a.second"), 2.0, constant(), 0.0);
  }

  @Test
  public void testSolveDiag() {
    List<Constraint> cons = ImmutableList.of(cof("a.first"), cof("a.second"));
    List<Property> props = ImmutableList.of(pof("a.first"), pof("a.second"), constant());
    DictionaryMatrix matrix = new DictionaryMatrix(cons, props);
    matrix.set(cof("a.first"), pof("a.first"), 1.0);
    matrix.set(cof("a.second"), pof("a.second"), 2.0);
    matrix.set(cof("a.first"), constant(), -3.0);
    matrix.set(cof("a.second"), constant(), -4.0);
    assertThat(matrix.getMapSolution()).containsExactly(pof("a.first"), 3.0, pof("a.second"), 2.0);
    Map<String, Map<String, Double>> rowMap = matrix.getTableSolution().rowMap();
    assertThat(rowMap.size()).isEqualTo(1);
    assertThat(rowMap.get("a")).containsExactly("first", 3.0, "second", 2.0);
  }
}
