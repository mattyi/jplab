package com.hzyi.jplab.core.model.kinematic;

import static com.hzyi.jplab.core.model.Constraint.cof;
import static com.hzyi.jplab.core.model.Property.pof;
import static com.hzyi.jplab.core.util.UnpackHelper.checkExistence;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.hzyi.jplab.core.model.Constraint;
import com.hzyi.jplab.core.model.Property;
import com.hzyi.jplab.core.util.Coordinate;
import com.hzyi.jplab.core.util.UnpackHelper;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

@EqualsAndHashCode
@Accessors(fluent = true)
@Builder(builderMethodName = "newBuilder", toBuilder = true)
@ToString
public class RopeModel extends Connector {

  private static final double STRETCH_LOWER_BOUNDARY = 0.995;
  private static final double STRETCH_UPPER_BOUNDARY = 1.005;

  @Getter private String name;
  @Getter private double length;
  private double relativePointUX;
  private double relativePointUY;
  private double relativePointVX;
  private double relativePointVY;
  @Builder.Default private double force = 0;
  @Getter private final SingleKinematicModel modelU;
  @Getter private final SingleKinematicModel modelV;

  public final KinematicModel.Type type() {
    return KinematicModel.Type.ROPE_MODEL;
  }

  @Override
  public Coordinate relativePointU() {
    return new Coordinate(relativePointUX, relativePointUY);
  }

  @Override
  public Coordinate relativePointV() {
    return new Coordinate(relativePointVX, relativePointVY);
  }

  @Override
  public double length() {
    return length;
  }

  @Override
  public Table<Constraint, Property, Double> codependentMultipliers(double timeStep) {
    System.out.printf("is stretched: %s, force: %f\n", isStretched(), force);
    if (isStretched()) {
      return ImmutableTable.<Constraint, Property, Double>builder()
          .put(
              cof(modelU, "vx-upwind"),
              pof(this, "impulse"),
              impulse(modelU, timeStep) * Math.cos(theta()))
          .put(
              cof(modelU, "vy-upwind"),
              pof(this, "impulse"),
              impulse(modelU, timeStep) * Math.sin(theta()))
          .put(
              cof(modelV, "vx-upwind"),
              pof(this, "impulse"),
              impulse(modelV, timeStep) * -Math.cos(theta()))
          .put(
              cof(modelV, "vy-upwind"),
              pof(this, "impulse"),
              impulse(modelV, timeStep) * -Math.sin(theta()))
          .put(cof(this, "vr-upwind-balance"), pof(modelU, "vx"), Math.cos(theta()))
          .put(cof(this, "vr-upwind-balance"), pof(modelU, "vy"), Math.sin(theta()))
          .put(cof(this, "vr-upwind-balance"), pof(modelV, "vx"), -Math.cos(theta()))
          .put(cof(this, "vr-upwind-balance"), pof(modelV, "vy"), -Math.sin(theta()))
          .put(cof(modelU, "ax-upwind-balance"), pof(this, "force"), Math.cos(theta()))
          .put(cof(modelU, "ay-upwind-balance"), pof(this, "force"), Math.sin(theta()))
          .put(cof(modelV, "ax-upwind-balance"), pof(this, "force"), -Math.cos(theta()))
          .put(cof(modelV, "ay-upwind-balance"), pof(this, "force"), -Math.sin(theta()))
          .put(cof(this, "ar-upwind-balance"), pof(modelU, "ax"), Math.cos(theta()))
          .put(cof(this, "ar-upwind-balance"), pof(modelU, "ay"), Math.sin(theta()))
          .put(cof(this, "ar-upwind-balance"), pof(modelV, "ax"), -Math.cos(theta()))
          .put(cof(this, "ar-upwind-balance"), pof(modelV, "ay"), -Math.sin(theta()))
          .build();
    }
    return ImmutableTable.of(cof(this, "no-force"), pof(this, "force"), 1.0);
  }

  @Override
  public List<Constraint> constraints() {
    if (isStretched()) {
      return ImmutableList.of(cof(this, "ar-upwind-balance"), cof(this, "vr-upwind-balance"));
    }
    return ImmutableList.of(cof(this, "no-force"));
  }

  @Override
  public List<Property> properties() {
    if (isStretched()) {
      return ImmutableList.of(pof(this, "force"), pof(this, "impulse"));
    }
    return ImmutableList.of(pof(this, "force"));
  }

  @Override
  public RopeModel merge(Map<String, ?> map) {
    UnpackHelper<RopeModelBuilder> helper = UnpackHelper.of(toBuilder(), map, RopeModel.class);
    return helper
        .unpack("model_u", SingleKinematicModel.class, RopeModelBuilder::modelU)
        .unpack("model_v", SingleKinematicModel.class, RopeModelBuilder::modelV)
        .unpack("force", Double.class, RopeModelBuilder::force)
        .getBuilder()
        .build();
  }

  public static RopeModel of(Map<String, ?> map) {
    RopeModelBuilder builder = newBuilder();
    UnpackHelper<RopeModelBuilder> helper = UnpackHelper.of(builder, map, RopeModel.class);
    BiFunction<RopeModelBuilder, String, RopeModelBuilder> collectorU =
        Connector.connectedModelExtractor(map, "Rope", "model_u");
    BiFunction<RopeModelBuilder, String, RopeModelBuilder> collectorV =
        Connector.connectedModelExtractor(map, "Rope", "model_v");
    helper.unpack("name", String.class, RopeModelBuilder::name, checkExistence());
    helper.unpack("model_u", String.class, collectorU, checkExistence());
    helper.unpack("model_v", String.class, collectorV, checkExistence());
    helper.unpack("relative_point_ux", Double.class, RopeModelBuilder::relativePointUX);
    helper.unpack("relative_point_uy", Double.class, RopeModelBuilder::relativePointUY);
    helper.unpack("relative_point_vx", Double.class, RopeModelBuilder::relativePointVX);
    helper.unpack("relative_point_vy", Double.class, RopeModelBuilder::relativePointVY);
    helper.unpack("length", Double.class, RopeModelBuilder::length, checkExistence());
    return helper.getBuilder().build();
  }

  public boolean isStretched() {
    // Preconditions.checkState(
    //     length * STRETCH_UPPER_BOUNDARY > distance(),
    //     "Rope %s is over stretched. Probably lower time step. pointU: %s, pointV: %s, length %s",
    //     name,
    //     pointU(),
    //     pointV(),
    //     distance(),
    //     length,
    //     distance() * STRETCH_UPPER_BOUNDARY);
    if (force <= 0) {
      return false;
    }
    return length * STRETCH_LOWER_BOUNDARY < distance();
  }

  private double distance() {
    return super.length();
  }

  private static double impulse(SingleKinematicModel model, double timeStep) {
    if (model.isRigidBody()) {
      return 1.0 / ((RigidBody) model).mass() * timeStep;
    }
    return 0;
  }

  public static class RopeModelBuilder implements ConnectorBuilder {}
}
