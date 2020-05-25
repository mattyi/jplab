package com.hzyi.jplab.core.model.kinematic;

import static com.hzyi.jplab.core.application.exceptions.Prechecks.checkFeature;
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
import lombok.experimental.Accessors;

@EqualsAndHashCode
@Accessors(fluent = true)
@Builder(builderMethodName = "newBuilder", toBuilder = true)
public class RodModel extends Connector {

  @Getter private String name;
  private double relativePointUX;
  private double relativePointUY;
  private double relativePointVX;
  private double relativePointVY;
  @Getter private SingleKinematicModel modelU;
  @Getter private SingleKinematicModel modelV;

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
  public Table<Constraint, Property, Double> codependentMultipliers(double timeStep) {
    return ImmutableTable.<Constraint, Property, Double>builder()
        .put(cof(modelU, "vx"), pof(this, "impulse"), impulse(modelU, timeStep) * Math.cos(theta()))
        .put(cof(modelU, "vy"), pof(this, "impulse"), impulse(modelU, timeStep) * Math.sin(theta()))
        .put(
            cof(modelV, "vx"), pof(this, "impulse"), impulse(modelV, timeStep) * -Math.cos(theta()))
        .put(
            cof(modelV, "vy"), pof(this, "impulse"), impulse(modelV, timeStep) * -Math.sin(theta()))
        .put(cof(this, "vr-upwind-balance"), pof(modelU, "vx"), Math.cos(theta()))
        .put(cof(this, "vr-upwind-balance"), pof(modelU, "vy"), Math.sin(theta()))
        .put(cof(this, "vr-upwind-balance"), pof(modelV, "vx"), -Math.cos(theta()))
        .put(cof(this, "vr-upwind-balance"), pof(modelV, "vy"), -Math.sin(theta()))
        .put(cof(modelU, "ax"), pof(this, "force"), Math.cos(theta()))
        .put(cof(modelU, "ay"), pof(this, "force"), Math.sin(theta()))
        .put(cof(modelV, "ax"), pof(this, "force"), -Math.cos(theta()))
        .put(cof(modelV, "ay"), pof(this, "force"), -Math.sin(theta()))
        .put(cof(this, "ar-upwind-balance"), pof(modelU, "ax"), Math.cos(theta()))
        .put(cof(this, "ar-upwind-balance"), pof(modelU, "ay"), Math.sin(theta()))
        .put(cof(this, "ar-upwind-balance"), pof(modelV, "ax"), -Math.cos(theta()))
        .put(cof(this, "ar-upwind-balances"), pof(modelV, "ay"), -Math.sin(theta()))
        .build();
  }

  @Override
  public List<Constraint> constraints() {
    return ImmutableList.of(cof(this, "ar-upwind-balance"), cof(this, "vr-upwind-balance"));
  }

  @Override
  public List<Property> properties() {
    return ImmutableList.of(pof(this, "impulse"), pof(this, "force"));
  }

  @Override
  public RodModel merge(Map<String, ?> map) {
    RodModelBuilder builder = toBuilder();
    UnpackHelper<RodModelBuilder> helper = UnpackHelper.of(builder, map, RodModel.class);
    helper.unpack("model_u", SingleKinematicModel.class, RodModelBuilder::modelU);
    helper.unpack("model_v", SingleKinematicModel.class, RodModelBuilder::modelV);
    return helper.getBuilder().build();
  }

  public static RodModel of(Map<String, ?> map) {
    RodModelBuilder builder = newBuilder();
    UnpackHelper<RodModelBuilder> helper = UnpackHelper.of(builder, map, RodModel.class);
    BiFunction<RodModelBuilder, String, RodModelBuilder> collectorU =
        Connector.connectedModelExtractor(map, "Rod", "model_u");
    BiFunction<RodModelBuilder, String, RodModelBuilder> collectorV =
        Connector.connectedModelExtractor(map, "Rod", "model_v");
    helper.unpack("model_u", String.class, collectorU, checkExistence());
    helper.unpack("model_v", String.class, collectorV, checkExistence());
    helper.unpack("name", String.class, RodModelBuilder::name, checkExistence());
    helper.unpack("relative_point_ux", Double.class, RodModelBuilder::relativePointUX);
    helper.unpack("relative_point_uy", Double.class, RodModelBuilder::relativePointUY);
    helper.unpack("relative_point_vx", Double.class, RodModelBuilder::relativePointVX);
    helper.unpack("relative_point_vy", Double.class, RodModelBuilder::relativePointVY);
    RodModel rod = helper.getBuilder().build();
    checkFeature(rod.modelU().vx() == 0.0, "unimplemented: modelU is not static initially");
    checkFeature(rod.modelU().vy() == 0.0, "unimplemented: modelU is not static initially");
    checkFeature(rod.modelV().ax() == 0.0, "unimplemented: modelV is not static initially");
    checkFeature(rod.modelV().ay() == 0.0, "unimplemented: modelV is not static initially");
    return rod;
  }

  private static double impulse(SingleKinematicModel model, double timeStep) {
    if (model.isRigidBody()) {
      return 1.0 / ((RigidBody) model).mass() * timeStep;
    }
    return 0;
  }

  public static class RodModelBuilder implements ConnectorBuilder {}
}
