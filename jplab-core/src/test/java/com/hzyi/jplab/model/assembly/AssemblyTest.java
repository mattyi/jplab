package com.hzyi.jplab.model.assembly;

import static com.google.common.truth.Truth.assertThat;

import com.hzyi.jplab.model.component.Component;
import com.hzyi.jplab.model.component.CircMassPoint;
import com.hzyi.jplab.model.component.DynamicComponent;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AssemblyTest {

  Assembly assembly;
  Component circMassPoint1;
  Component circMassPoint2;

  @Before
  public void setUp() {
    circMassPoint1 = CircMassPoint
        .newBuilder()
        .setName("circMassPoint1")
        .setX(0.1)
        .setY(0.2)
        .setDirX(0.3)
        .setDirY(0.4)
        .setVX(0.5)
        .setVY(0.6)
        .setMass(0.7)
        .setMomentOfInertia(0.8)
        .setRadius(0.9)
        .build();
    circMassPoint2 = CircMassPoint
        .newBuilder()
        .setName("circMassPoint2")
        .setX(1.1)
        .setY(1.2)
        .setDirX(1.3)
        .setDirY(1.4)
        .setVX(1.5)
        .setVY(1.6)
        .setMass(1.7)
        .setMomentOfInertia(1.8)
        .setRadius(1.9)
        .build();
    assembly = Assembly
        .newBuilder()
        .setName("assembly")
        .add(circMassPoint1)
        .add(circMassPoint2)
        .build();
  }

  @Test
  public void testBuilder() {
    AssemblyState initState = assembly.getInitialAssemblyState();
    assertThat(initState.get("circMassPoint1", DynamicComponent.V_X())).isEqualTo(0.5);
    assertThat(initState.get("circMassPoint2", DynamicComponent.V_X())).isEqualTo(1.5);
    assertThat(assembly.getComponent("circMassPoint1")).isSameAs(circMassPoint1);
  }



}