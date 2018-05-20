package com.hzyi.jplab.model.component;

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
            .setRadius(0.7)
            .build();
    ComponentState componentState = circMassPoint.getInitialComponentState();
    assertThat(componentState.getComponent()).isSameAs(circMassPoint);
    assertThat(componentState.get(StaticComponent.LOC_X())).isEqualTo(0.1);
    assertThat(componentState.get(StaticComponent.LOC_Y())).isEqualTo(0.2);
    assertThat(componentState.get(StaticComponent.DIR_X())).isEqualTo(0.3);
    assertThat(componentState.get(StaticComponent.DIR_Y())).isEqualTo(0.4);
    assertThat(componentState.get(DynamicComponent.V_X())).isEqualTo(0.5);
    assertThat(componentState.get(DynamicComponent.V_Y())).isEqualTo(0.6);
    assertThat(circMassPoint.getRadius()).isEqualTo(0.7);
  }

}