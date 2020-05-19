package com.hzyi.jplab.core.model.kinematic;

import static com.hzyi.jplab.core.application.exceptions.Prechecks.checkFeature;
import static com.hzyi.jplab.core.util.UnpackHelper.checkExistence;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
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
  public Table<String, String, Double> codependentMultipliers(double timeStep) {
    return ImmutableTable.<String, String, Double>builder()
        .put(
            Property.format(modelU, "vx"),
            Property.format(this, "force"),
            impulse(modelU, timeStep) * Math.cos(theta()))
        .put(
            Property.format(modelU, "vy"),
            Property.format(this, "force"),
            impulse(modelU, timeStep) * Math.sin(theta()))
        .put(
            Property.format(modelV, "vx"),
            Property.format(this, "force"),
            impulse(modelV, timeStep) * -Math.cos(theta()))
        .put(
            Property.format(modelV, "vy"),
            Property.format(this, "force"),
            impulse(modelV, timeStep) * -Math.sin(theta()))
        .put(Property.format(this, "force"), Property.format(modelU, "vx"), Math.cos(theta()))
        .put(Property.format(this, "force"), Property.format(modelU, "vy"), Math.sin(theta()))
        // This assumes a connected model either is static or exposes unknown ax, ay fields, which
        // may be problematic in the future
        .put(Property.format(this, "force"), Property.format(modelV, "vx"), -Math.cos(theta()))
        .put(Property.format(this, "force"), Property.format(modelV, "vy"), -Math.sin(theta()))
        .put(Property.format(modelU, "ax"), Property.format(this, "force2"), Math.cos(theta()))
        .put(Property.format(modelU, "ay"), Property.format(this, "force2"), Math.sin(theta()))
        .put(Property.format(modelV, "ax"), Property.format(this, "force2"), -Math.cos(theta()))
        .put(Property.format(modelV, "ay"), Property.format(this, "force2"), -Math.sin(theta()))
        .put(Property.format(this, "force2"), Property.format(modelU, "ax"), Math.cos(theta()))
        .put(Property.format(this, "force2"), Property.format(modelU, "ay"), Math.sin(theta()))
        // This assumes a connected model either is static or exposes unknown ax, ay fields, which
        // may be problematic in the future
        .put(Property.format(this, "force2"), Property.format(modelV, "ax"), -Math.cos(theta()))
        .put(Property.format(this, "force2"), Property.format(modelV, "ay"), -Math.sin(theta()))
        .build();
  }

  @Override
  public List<String> codependentProperties() {
    return ImmutableList.of(Property.format(this, "force"), Property.format(this, "force2"));
  }

  @Override
  public RodModel merge(Map<String, ?> map) {
    RodConnectorBuilder builder = toBuilder();
    UnpackHelper<RodConnectorBuilder> helper = UnpackHelper.of(builder, map, RodModel.class);
    helper.unpack("model_u", SingleKinematicModel.class, RodConnectorBuilder::modelU);
    helper.unpack("model_v", SingleKinematicModel.class, RodConnectorBuilder::modelV);
    return helper.getBuilder().build();
  }

  public static RodModel of(Map<String, ?> map) {
    RodConnectorBuilder builder = newBuilder();
    UnpackHelper<RodConnectorBuilder> helper = UnpackHelper.of(builder, map, RodModel.class);
    BiFunction<RodConnectorBuilder, String, RodConnectorBuilder> extractorU =
        Connector.connectedModelExtractor(map, "Rod", "model_u");
    BiFunction<RodConnectorBuilder, String, RodConnectorBuilder> extractorV =
        Connector.connectedModelExtractor(map, "Rod", "model_u");
    helper.unpack("model_u", String.class, extractorU, checkExistence());
    helper.unpack("model_v", String.class, extractorV, checkExistence());
    helper.unpack("name", String.class, RodConnectorBuilder::name, checkExistence());
    helper.unpack("relative_point_ux", Double.class, RodConnectorBuilder::relativePointUX);
    helper.unpack("relative_point_uy", Double.class, RodConnectorBuilder::relativePointUY);
    helper.unpack("relative_point_vx", Double.class, RodConnectorBuilder::relativePointVX);
    helper.unpack("relative_point_vy", Double.class, RodConnectorBuilder::relativePointVY);
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

  public static class RodConnectorBuilder implements ConnectorBuilder {}
}
