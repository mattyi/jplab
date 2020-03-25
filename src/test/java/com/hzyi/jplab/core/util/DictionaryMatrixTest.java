package com.hzyi.jplab.core.util;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;

import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class DictionaryMatrixTest {

  @Test
  public void testCreate() {
    DictionaryMatrix matrix = new DictionaryMatrix("first", "CONST", "second");
    assertThat(matrix.getRow("first")).containsExactly("first", 0.0, "second", 0.0, "CONST", 0.0);
    assertThat(matrix.getCol("second")).containsExactly("first", 0.0, "second", 0.0);
  }

  @Test
  public void testCreateFailOnlyOneKey() {
    try {
      DictionaryMatrix matrix = new DictionaryMatrix("first");
      fail();
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage())
          .isEqualTo("only one key: expecting CONST and at least one other key, got first");
    }
  }

  @Test
  public void testCreateFailNoConst() {
    try {
      DictionaryMatrix matrix = new DictionaryMatrix("first", "second");
      fail();
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage()).isEqualTo("CONST not in keys");
    }
  }

  @Test
  public void testCreateFailDupKeys() {
    try {
      DictionaryMatrix matrix = new DictionaryMatrix("first", "first", "CONST");
      fail();
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage()).isEqualTo("non-unique key: first");
    }
  }

  @Test
  public void testSet() {
    DictionaryMatrix matrix = new DictionaryMatrix("first", "CONST", "second");
    matrix.set("first", "first", 2.0);
    assertThat(matrix.get("first", "first")).isEqualTo(2.0);
  }

  @Test
  public void testAdd() {
    DictionaryMatrix matrix = new DictionaryMatrix("first", "CONST", "second");
    matrix.add("first", "second", 2.0);
    assertThat(matrix.get("first", "second")).isEqualTo(2.0);
    matrix.add("first", "second", 2.0);
    assertThat(matrix.get("first", "second")).isEqualTo(4.0);
  }

  @Test
  public void testSetAndAddFail() {
    DictionaryMatrix matrix = new DictionaryMatrix("first", "CONST", "second");
    try {
      matrix.add("not_first", "second", 2.0);
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage()).isEqualTo("non-existing row: not_first");
    }

    try {
      matrix.set("CONST", "second", 2.0);
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage()).isEqualTo("non-existing row: CONST");
    }

    try {
      matrix.set("first", "not_second", 2.0);
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage()).isEqualTo("non-existing col: not_second");
    }
  }

  @Test
  public void testGetRowAndCol() {
    DictionaryMatrix matrix = new DictionaryMatrix("first", "CONST", "second");
    matrix.set("first", "first", 1.0);
    matrix.set("first", "second", 2.0);
    assertThat(matrix.getRow("first")).containsExactly("first", 1.0, "second", 2.0, "CONST", 0.0);
    assertThat(matrix.getCol("first")).containsExactly("first", 1.0, "second", 0.0);
  }

  @Test
  public void testSolveDiag() {
    DictionaryMatrix matrix = new DictionaryMatrix("first", "CONST", "second");
    matrix.set("first", "first", 1.0);
    matrix.set("second", "second", 2.0);
    matrix.set("first", "CONST", -3.0);
    matrix.set("second", "CONST", -4.0);
    Map<String, Double> answer = matrix.solve();
    assertThat(answer).containsExactly("first", 3.0, "second", 2.0);
  }
}
