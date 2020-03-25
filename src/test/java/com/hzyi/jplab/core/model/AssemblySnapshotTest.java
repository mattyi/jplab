package com.hzyi.jplab.core.model;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableMap;
import com.hzyi.jplab.core.model.kinematic.MassPoint;
import com.hzyi.jplab.core.model.kinematic.SpringModel;
import com.hzyi.jplab.core.model.kinematic.StaticModel;
import com.hzyi.jplab.core.util.DictionaryMatrix;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class AssemblySnapshotTest {

  private MassPoint massPoint;
  private SpringModel springModel;
  private StaticModel staticModel;
  private AssemblySnapshot snapshot;

  @Before
  public void setUp() {
    massPoint = MassPoint.newBuilder().name("mass").mass(1.0).build();
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
  public void testPackUnpack() {
    assertThat(massPoint.unpack(massPoint.pack())).isEqualTo(massPoint);
    assertThat(springModel.unpack(springModel.pack())).isEqualTo(springModel);
    assertThat(staticModel.unpack(staticModel.pack())).isEqualTo(staticModel);
  }

  @Test
  public void testPackUpdateUnpack() {
    Map<String, Object> massPointMap = massPoint.pack();
    massPointMap.put("x", (Double) 10.0);
    MassPoint updatedMassPoint = massPoint.unpack(massPointMap);
    assertThat(updatedMassPoint).isEqualTo(massPoint.toBuilder().x(10.0).build());
    Map<String, Object> springMap = springModel.pack();
    springMap.put("connecting_model_a", updatedMassPoint);
    assertThat(springModel.unpack(springMap))
        .isEqualTo(springModel.toBuilder().connectingModelA(updatedMassPoint).build());
  }

  @Test
  public void testUnpack() {
    snapshot = snapshot.unpack(ImmutableMap.<String, Double>of("mass.x", -10.0, "mass.ax", 5.0));
    MassPoint newMassPoint = massPoint.toBuilder().x(-10.0).ax(5.0).build();
    SpringModel newSpring = springModel.toBuilder().connectingModelA(newMassPoint).build();
    assertThat((MassPoint) snapshot.get("mass")).isEqualTo(newMassPoint);
    assertThat((StaticModel) snapshot.get("wall")).isEqualTo(staticModel);
    assertThat((SpringModel) snapshot.get("spring")).isEqualTo(newSpring);
  }

  @Test
  public void testToMatrix() {
    DictionaryMatrix matrix = snapshot.toMatrix();
    assertThat(matrix.getRow("mass.ax"))
        .containsExactly("mass.ax", -1.0, "mass.ay", 0.0, "CONST", 1.0);
    assertThat(matrix.getRow("mass.ay"))
        .containsExactly("mass.ax", 0.0, "mass.ay", -1.0, "CONST", 0.0);
    Map<String, Double> answer = matrix.solve();
    assertThat(answer).containsExactly("mass.ax", 1.0, "mass.ay", 0.0);
  }
}
