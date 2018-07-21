package com.hzyi.jplab.core.model;

import static com.google.common.truth.Truth.assertThat;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Test;

@RunWith(JUnit4.class)
public class CircMassPointTest {

  @Test
  public void testBuilder() {
    CircMassPoint circMassPoint =
        CircMassPoint.newBuilder()
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
    ComponentState componentState = circMassPoint.getInitialComponentState();
    assertThat(componentState.getComponent()).isSameAs(circMassPoint);
    assertThat(componentState.get(Field.LOCX)).isEqualTo(0.1);
    assertThat(componentState.get(Field.LOCY)).isEqualTo(0.2);
    assertThat(componentState.get(Field.DIRX)).isEqualTo(0.3);
    assertThat(componentState.get(Field.DIRY)).isEqualTo(0.4);
    assertThat(componentState.get(Field.VX)).isEqualTo(0.5);
    assertThat(componentState.get(Field.VY)).isEqualTo(0.6);
    assertThat(circMassPoint.getMass()).isEqualTo(0.7);
    assertThat(circMassPoint.getMomentOfInertia()).isEqualTo(0.8);
    assertThat(circMassPoint.getRadius()).isEqualTo(0.9);
  }

}