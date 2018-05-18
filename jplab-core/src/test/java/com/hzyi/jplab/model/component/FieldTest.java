package com.hzyi.jplab.model.component;

import static com.google.common.truth.Truth.assertThat;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Test;

@RunWith(JUnit4.class)
public class FieldTest {

  @Test
  public void testOf() {
    Field.addField("field1");
    assertThat(Field.of("field1")).isNotNull();
    Field.reset();
  }

  @Test
  public void testAddField() {
    assertThat(Field.addField("field1")).isNotNull();
    Field.reset();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testOfWithException() {
    Field.addField("field1");
    Field.of("field2");
    Field.reset();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddFieldWithException() {
    Field.addField("field1");
    Field.addField("field1");
    Field.reset();
  }
}