package com.hzyi.jplab.core.controller;

import static com.google.common.truth.Truth.assertThat;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Test;

@RunWith(JUnit4.class)
public class ControllerImplTest {

  @Test
  public void testGetParameter() {
    Controller controller = new ControllerImpl();
    Parameter<String> param1 = Parameter.newStringParameter("val1", "name1");
    Parameter<String> param2 = Parameter.newStringParameter("val2", "name2");
    controller.addParameter(param1);
    controller.addParameter(param2);
    assertThat(controller.getParameters()).containsExactly(param1, param2);
  }

  @Test
  public void testGetParameters() {
    Controller controller = new ControllerImpl();
    Parameter<String> param1 = Parameter.newStringParameter("val1", "name1");
    Parameter<String> param2 = Parameter.newStringParameter("val2", "name2");
    controller.addParameter(param1);
    controller.addParameter(param2);
    assertThat(controller.getParameter("name1")).isEqualTo(param1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetParametersWithException() {
    Controller controller = new ControllerImpl();
    Parameter<String> param1 = Parameter.newStringParameter("val1", "name1");
    Parameter<String> param2 = Parameter.newStringParameter("val2", "name2");
    controller.addParameter(param1);
    controller.addParameter(param2);
    controller.getParameter("name3");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddParameterWithException() {
    Controller controller = new ControllerImpl();
    Parameter<String> param1 = Parameter.newStringParameter("val1", "name1");
    Parameter<String> param2 = Parameter.newStringParameter("val2", "name1");
    controller.addParameter(param1);
    controller.addParameter(param2);
  }
}