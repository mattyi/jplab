package com.hzyi.jplab.model.component;

import static com.google.common.truth.Truth.assertThat;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@RunWith(JUnit4.class)
public class ComponentStateTest {
  
  Field field1;
  Field field2;
  Component component;

  @Before
  public void setUp() {
    field1 = Field.addField("field1");
    field2 = Field.addField("field2");
    component = FakeComponent.newBuilder().setName("component").build();
  }

  @After
  public void tearDown() {
    Field.reset();
  }

  @Test
  public void testBuilder() {
    ComponentState componentState = ComponentState.newBuilder(component)
        .set(field1, 1.0)
        .set(field2, 2.0)
        .build();
    assertThat(componentState.getComponent()).isSameAs(component);
    assertThat(componentState.get(field1)).isEqualTo(1.0);
    assertThat(componentState.get(field2)).isEqualTo(2.0);
  }

  @Test
  public void testBuilderSetOverrides() {
    ComponentState componentState = ComponentState.newBuilder(component)
        .set(field1, 1.0)
        .set(field2, 2.0)
        .set(field1, 3.0)
        .build();
    assertThat(componentState.getComponent()).isSameAs(component);
    assertThat(componentState.get(field1)).isEqualTo(3.0);
    assertThat(componentState.get(field2)).isEqualTo(2.0);
  }

  @Test
  public void testSet() {
    ComponentState componentState = ComponentState.newBuilder(component)
        .set(field1, 1.0)
        .set(field2, 2.0)
        .build();
    componentState.set(field1, 3.0);
    assertThat(componentState.get(field1)).isEqualTo(3.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetWithException() {
    ComponentState componentState = ComponentState.newBuilder(component)
        .set(field1, 1.0)
        .build();
    componentState.set(field2, 3.0);
  }
}