package com.hzyi.jplab.core.model;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableTable;
import com.hzyi.jplab.core.application.Application;
import com.hzyi.jplab.core.model.kinematic.MassPoint;
import com.hzyi.jplab.core.model.kinematic.SpringModel;
import com.hzyi.jplab.core.model.kinematic.StaticModel;
import com.hzyi.jplab.core.painter.CoordinateTransformer;
import com.hzyi.jplab.core.painter.PainterFactory;
import com.hzyi.jplab.core.util.DictionaryMatrix;
import java.util.Map;
import javafx.scene.canvas.Canvas;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class AssemblyTest {

  private MassPoint massPoint;
  private SpringModel springModel;
  private StaticModel staticModel;
  private Assembly assembly;
  private PainterFactory painterFactory;

  @Before
  public void setUp() {
    massPoint = MassPoint.newBuilder().name("mass").mass(1.0).build();
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
    Canvas canvas = new Canvas(1, 1);
    painterFactory = new PainterFactory(canvas, new CoordinateTransformer(canvas, 1));
  }

  @Test
  public void testPackUnpack() {
    assertThat(massPoint.merge(massPoint.pack())).isEqualTo(massPoint);
    assertThat(springModel.merge(springModel.pack())).isEqualTo(springModel);
    assertThat(staticModel.merge(staticModel.pack())).isEqualTo(staticModel);
  }

  @Test
  public void testPackUpdateUnpack() {
    Map<String, Object> massPointMap = massPoint.pack();
    massPointMap.put("x", (Double) 10.0);
    MassPoint updatedMassPoint = massPoint.merge(massPointMap);
    assertThat(updatedMassPoint).isEqualTo(massPoint.toBuilder().x(10.0).build());
    Map<String, Object> springMap = springModel.pack();
    springMap.put("model_u", updatedMassPoint);
    assertThat(springModel.merge(springMap))
        .isEqualTo(springModel.toBuilder().modelU(updatedMassPoint).build());
  }

  @Test
  public void testUnpack() {
    assembly =
        assembly.merge(
            ImmutableTable.<String, String, Double>builder()
                .put("mass", "x", -10.0)
                .put("mass", "ax", 5.0)
                .build());
    MassPoint newMassPoint = massPoint.toBuilder().x(-10.0).ax(5.0).build();
    SpringModel newSpring = springModel.toBuilder().modelU(newMassPoint).build();
    assertThat((MassPoint) assembly.getKinematicModel("mass")).isEqualTo(newMassPoint);
    assertThat((StaticModel) assembly.getKinematicModel("wall")).isEqualTo(staticModel);
    assertThat((SpringModel) assembly.getConnector("spring")).isEqualTo(newSpring);
  }

  @Test
  public void testToMatrix() {
    Application.init(
        null,
        assembly,
        null,
        null,
        CoordinateTransformer.getTestingCoordinateTransformer(),
        painterFactory,
        null,
        1.0);
    DictionaryMatrix matrix = assembly.getCodependentMatrix(1.0);
    assertThat(matrix.getRow(Constraint.parse("mass.ax-upwind-balance")))
        .containsExactly(
            Property.parse("mass.x"),
            0.0,
            Property.parse("mass.y"),
            0.0,
            Property.parse("mass.vx"),
            0.0,
            Property.parse("mass.vy"),
            0.0,
            Property.parse("mass.ax"),
            -1.0,
            Property.parse("mass.ay"),
            0.0,
            Property.constant(),
            1.0);
    assertThat(matrix.getRow(Constraint.parse("mass.ay-upwind-balance")))
        .containsExactly(
            Property.parse("mass.x"),
            0.0,
            Property.parse("mass.y"),
            0.0,
            Property.parse("mass.vx"),
            0.0,
            Property.parse("mass.vy"),
            0.0,
            Property.parse("mass.ax"),
            0.0,
            Property.parse("mass.ay"),
            -1.0,
            Property.constant(),
            0.0);
    Map<Property, Double> answer = matrix.getMapSolution();
    assertThat(answer)
        .containsExactly(
            Property.parse("mass.x"),
            0.0,
            Property.parse("mass.y"),
            0.0,
            Property.parse("mass.vx"),
            0.0,
            Property.parse("mass.vy"),
            0.0,
            Property.parse("mass.ax"),
            1.0,
            Property.parse("mass.ay"),
            -0.0);
    Application.reset();
  }
}
