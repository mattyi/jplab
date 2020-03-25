package com.hzyi.jplab.core.timeline;

import static com.google.common.truth.Truth.assertThat;

import com.hzyi.jplab.core.model.AssemblySnapshot;
import com.hzyi.jplab.core.model.kinematic.MassPoint;
import com.hzyi.jplab.core.model.kinematic.SpringModel;
import com.hzyi.jplab.core.model.kinematic.StaticModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class NumericTimelineTest {

  private MassPoint massPoint;
  private SpringModel springModel;
  private StaticModel staticModel;
  private AssemblySnapshot snapshot;

  @Before
  public void setUp() {
    massPoint = MassPoint.newBuilder().name("mass").mass(1.0).vx(1).build();
    staticModel = StaticModel.newBuilder().name("wall").x(2.0).build();
    springModel =
        SpringModel.newBuilder()
            .name("spring")
            .stiffness(1.0)
            .originalLength(1.0)
            .connectingModelA(massPoint)
            .connectingModelB(staticModel)
            .build();
    snapshot =
        AssemblySnapshot.newBuilder()
            .kinematicModel("mass", massPoint)
            .kinematicModel("wall", staticModel)
            .kinematicModel("spring", springModel)
            .build();
  }

  @Test
  public void testAdvanceTimeStep() {
    NumericTimeline timeline = new NumericTimeline(snapshot);
    timeline.advance(1);
    AssemblySnapshot snapshot = timeline.getLatestAssemblySnapshot();
    MassPoint newMassPoint = (MassPoint) snapshot.get("mass");
    assertThat(newMassPoint.x()).isEqualTo(1.0);
    assertThat(newMassPoint.y()).isEqualTo(0.0);
    StaticModel newStaticModel = (StaticModel) snapshot.get("wall");
    assertThat(newStaticModel.x()).isEqualTo(2.0);
    assertThat(newStaticModel.y()).isEqualTo(0.0);
    SpringModel newSpring = (SpringModel) snapshot.get("spring");
    assertThat(newSpring.connectingModelA()).isEqualTo(newMassPoint);
    assertThat(newSpring.connectingModelB()).isEqualTo(newStaticModel);
  }

  @Test
  public void testAdjustInternalState() {}
}
