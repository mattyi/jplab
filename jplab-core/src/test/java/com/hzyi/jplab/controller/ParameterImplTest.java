package com.hzyi.jplab.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class ParameterImplTest {

  @Test
  public void testGetName() {
    Parameter<String> parameter = Parameter.newStringParameter("initValue", "name");
    assertEquals(parameter.getName(), "name");
  }

  @Test
  public void testGetValue() {
    Parameter<String> parameter = Parameter.newStringParameter("initValue", "name");
    assertEquals(parameter.getName(), "initValue");
  }

  @Test
  public void testSetValueUpdatesObserver() {
    
  }

  @Test
  public void testAddObserver() {

  }

}