package com.hzyi.jplab.core.model;

import static com.google.common.truth.Truth.assertThat;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Test;

@RunWith(JUnit4.class)
public class FieldTest {

  @Before
  public void setUp() {
    Field.reset();
  }

  @Test
  public void testOf() {
    Field.add("field1");
    assertThat(Field.of("field1")).isNotNull();
    Field.reset();
  }

  @Test
  public void testAddField() {
    assertThat(Field.add("field1")).isNotNull();
    Field.reset();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testOfWithException() {
    Field.add("field1");
    Field.of("field2");
    Field.reset();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddFieldWithException() {
    Field.add("field1");
    Field.add("field1");
    Field.reset();
  }
}