package com.hzyi.jplab.core.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class IntervalDoubleParameterTest {
  
  @Test
  public void testSetValue() {
    IntervalDoubleParameter parameter = 
        new IntervalDoubleParameter(0.0, "parameter", 0.0, 5.0, 10);
    parameter.setValue(2.4);
    assertThat(parameter.getValue()).isEqualTo(2.5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetValueException() {
    IntervalDoubleParameter parameter = 
        new IntervalDoubleParameter(0.0, "parameter", 0.0, 5.0, 5);
    parameter.setValue(5.5);
  }


}