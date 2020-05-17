package com.hzyi.jplab.core.model.kinematic;

import static com.hzyi.jplab.core.util.UnpackHelper.checkExistence;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.hzyi.jplab.core.application.exceptions.Prechecks;
import com.hzyi.jplab.core.model.Property;
import com.hzyi.jplab.core.util.Coordinate;
import com.hzyi.jplab.core.util.CoordinateSystem;
import com.hzyi.jplab.core.util.UnpackHelper;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

@EqualsAndHashCode
@Accessors(fluent = true)
@Builder(builderMethodName = "newBuilder", toBuilder = true)
public class RodModel extends ConnectingModel {

  @Getter private String name;
  private double relativeConnectingPointAX;
  private double relativeConnectingPointAY;
  private double relativeConnectingPointBX;
  private double relativeConnectingPointBY;
  @Getter private SingleKinematicModel connectingModelA;
  @Getter private SingleKinematicModel connectingModelB;

  public final KinematicModel.Type type() {
    return KinematicModel.Type.ROPE_MODEL;
  }

  @Override
  public Coordinate relativeConnectingPointA() {
    return new Coordinate(relativeConnectingPointAX, relativeConnectingPointAY);
  }

  @Override
  public Coordinate relativeConnectingPointB() {
    return new Coordinate(relativeConnectingPointBX, relativeConnectingPointBY);
  }

  @Override
  public CoordinateSystem bodyCoordinateSystem() {
    throw new UnsupportedOperationException("not needed yet");
  }

  @Override
  public Table<String, String, Double> codependentMultipliers(double timeStep) {
    return ImmutableTable.<String, String, Double>builder()
        .put(
            Property.format(connectingModelA, "vx"),
            Property.format(this, "force"),
            velocityCompensator(connectingModelA) * Math.cos(theta()))
        .put(
            Property.format(connectingModelA, "vy"),
            Property.format(this, "force"),
            velocityCompensator(connectingModelA) * Math.sin(theta()))
        .put(
            Property.format(connectingModelB, "vx"),
            Property.format(this, "force"),
            velocityCompensator(connectingModelB) * -Math.cos(theta()))
        .put(
            Property.format(connectingModelB, "vy"),
            Property.format(this, "force"),
            velocityCompensator(connectingModelB) * -Math.sin(theta()))
        .put(
            Property.format(this, "force"),
            Property.format(connectingModelA, "vx"),
            Math.cos(theta()))
        .put(
            Property.format(this, "force"),
            Property.format(connectingModelA, "vy"),
            Math.sin(theta()))
        // This assumes a connected model either is static or exposes unknown ax, ay fields, which
        // may be problematic in the future
        .put(
            Property.format(this, "force"),
            Property.format(connectingModelB, "vx"),
            -Math.cos(theta()))
        .put(
            Property.format(this, "force"),
            Property.format(connectingModelB, "vy"),
            -Math.sin(theta()))
        .put(
            Property.format(connectingModelA, "ax"),
            Property.format(this, "force2"),
            Math.cos(theta()))
        .put(
            Property.format(connectingModelA, "ay"),
            Property.format(this, "force2"),
            Math.sin(theta()))
        .put(
            Property.format(connectingModelB, "ax"),
            Property.format(this, "force2"),
            -Math.cos(theta()))
        .put(
            Property.format(connectingModelB, "ay"),
            Property.format(this, "force2"),
            -Math.sin(theta()))
        .put(
            Property.format(this, "force2"),
            Property.format(connectingModelA, "ax"),
            Math.cos(theta()))
        .put(
            Property.format(this, "force2"),
            Property.format(connectingModelA, "ay"),
            Math.sin(theta()))
        // This assumes a connected model either is static or exposes unknown ax, ay fields, which
        // may be problematic in the future
        .put(
            Property.format(this, "force2"),
            Property.format(connectingModelB, "ax"),
            -Math.cos(theta()))
        .put(
            Property.format(this, "force2"),
            Property.format(connectingModelB, "ay"),
            -Math.sin(theta()))
        .build();
  }

  @Override
  public List<String> codependentProperties() {
    return ImmutableList.of(Property.format(this, "force"), Property.format(this, "force2"));
  }

  @Override
  public RodModel merge(Map<String, ?> map) {
    RodModelBuilder builder = toBuilder();
    UnpackHelper<RodModelBuilder> helper = UnpackHelper.of(builder, map, RodModel.class);
    helper.unpack(
        "connecting_model_a", SingleKinematicModel.class, RodModelBuilder::connectingModelA);
    helper.unpack(
        "connecting_model_b", SingleKinematicModel.class, RodModelBuilder::connectingModelB);
    return helper.getBuilder().build();
  }

  public static RodModel of(Map<String, ?> map) {
    RodModelBuilder builder = newBuilder();
    UnpackHelper<RodModelBuilder> helper = UnpackHelper.of(builder, map, RodModel.class);
    helper.unpack(
        "component_a",
        String.class,
        ConnectingModel.newExtractor(map, "Rod", "component_a"),
        checkExistence());
    helper.unpack(
        "component_b",
        String.class,
        ConnectingModel.newExtractor(map, "Rod", "component_b"),
        checkExistence());
    helper.unpack("name", String.class, RodModelBuilder::name, checkExistence());
    helper.unpack(
        "relative_connecting_point_ax", Double.class, RodModelBuilder::relativeConnectingPointAX);
    helper.unpack(
        "relative_connecting_point_ay", Double.class, RodModelBuilder::relativeConnectingPointAY);
    helper.unpack(
        "relative_connecting_point_bx", Double.class, RodModelBuilder::relativeConnectingPointBX);
    helper.unpack(
        "relative_connecting_point_by", Double.class, RodModelBuilder::relativeConnectingPointBY);
    RodModel rod = helper.getBuilder().build();
    Prechecks.checkFeature(
        rod.connectingModelA().vx() == 0.0,
        "unimplemented: model connected to rod is not static initially");
    Prechecks.checkFeature(
        rod.connectingModelA().vy() == 0.0,
        "unimplemented: model connected to rod is not static initially");
    Prechecks.checkFeature(
        rod.connectingModelA().ax() == 0.0,
        "unimplemented: model connected to rod is not static initially");
    Prechecks.checkFeature(
        rod.connectingModelA().ay() == 0.0,
        "unimplemented: model connected to rod is not static initially");
    return rod;
  }

  private static double velocityCompensator(SingleKinematicModel model) {
    if (model.isRigidBody()) {
      return 1.0 / ((RigidBody) model).mass();
    }
    return 0;
  }

  public static class RodModelBuilder implements ConnectingModelBuilder {}
}
