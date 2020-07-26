package com.hzyi.jplab.core.timeline;

import static com.google.common.truth.Truth.assertThat;

import com.hzyi.jplab.core.application.Application;
import com.hzyi.jplab.core.model.Assembly;
import com.hzyi.jplab.core.model.kinematic.MassPoint;
import com.hzyi.jplab.core.model.kinematic.SpringModel;
import com.hzyi.jplab.core.model.kinematic.StaticModel;
import com.hzyi.jplab.core.painter.CoordinateTransformer;
import com.hzyi.jplab.core.painter.PainterFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class NumericTimelineTest {

  private MassPoint massPoint;
  private SpringModel springModel;
  private StaticModel staticModel;
  private Assembly assembly;

  @Before
  public void setUp() {
    massPoint = MassPoint.newBuilder().name("mass").mass(1.0).vx(1).build();
    staticModel = StaticModel.newBuilder().name("wall").x(2.0).build();
    springModel =
        SpringModel.newBuilder()
            .name("spring")
            .stiffness(1.0)
            .unstretchedLength(1.0)
            .modelU(massPoint)
            .modelV(staticModel)
            .build();
    assembly =
        Assembly.empty()
            .withComponent(massPoint)
            .withComponent(staticModel)
            .withComponent(springModel);
    assembly.makeImmutable();
  }

  @Test
  public void testAdvanceTimeStep() {
    Application.init(
        null,
        assembly,
        null,
        null,
        CoordinateTransformer.getTestingCoordinateTransformer(),
        PainterFactory.getTestingPainterFactory(),
        null,
        1.0);
    NumericTimeline timeline = new NumericTimeline(assembly);
    timeline.advance(1);
    Assembly assembly = timeline.getLatestAssembly();
    MassPoint newMassPoint = (MassPoint) assembly.getComponent("mass");
    assertThat(newMassPoint.x()).isEqualTo(1.0);
    assertThat(newMassPoint.y()).isEqualTo(0.0);
    StaticModel newStaticModel = (StaticModel) assembly.getComponent("wall");
    assertThat(newStaticModel.x()).isEqualTo(2.0);
    assertThat(newStaticModel.y()).isEqualTo(0.0);
    SpringModel newSpring = (SpringModel) assembly.getComponent("spring");
    assertThat(newSpring.modelU()).isEqualTo(newMassPoint);
    assertThat(newSpring.modelV()).isEqualTo(newStaticModel);
    Application.reset();
  }
}
