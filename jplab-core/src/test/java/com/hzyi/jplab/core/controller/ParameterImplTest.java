package com.hzyi.jplab.core.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.mockito.invocation.InvocationOnMock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;


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
    assertEquals(parameter.getValue(), "initValue");
  }

  @Test
  public void testSetValueUpdatesObserver() {
    Observer<String> observer = mock(Observer.class);
    Parameter<String> parameter = Parameter.newStringParameter("initValue", "name");
    parameter.addObserver(observer);
    doAnswer(
        new Answer<Void>() {
          public Void answer(InvocationOnMock invocation) {
            String newValue = (String)invocation.getArguments()[0];
            assertEquals(newValue, "new-value");
            return null;
          }
        }
    )
    .when(observer)
    .update(Mockito.<String>any());
    parameter.setValue("new-value");
  }

}