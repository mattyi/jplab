package com.hzyi.jplab.core.model.kinematic;

import static com.hzyi.jplab.core.util.UnpackHelper.checkExistence;
import static com.hzyi.jplab.core.util.UnpackHelper.checkPositivity;

import com.google.common.collect.Table;
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
public class RopeModel extends ConnectingModel {

  @Getter private String name;
  @Getter private double length;
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
    throw new UnsupportedOperationException("not needed yet");
  }

  @Override
  public List<String> codependentPropertys() {
    throw new UnsupportedOperationException("not needed yet");
  }

  @Override
  public RopeModel merge(Map<String, ?> map) {
    RopeModelBuilder builder = toBuilder();
    UnpackHelper<RopeModelBuilder> helper = UnpackHelper.of(builder, map, SpringModel.class);
    helper.unpack(
        "connecting_model_a",
        SingleKinematicModel.class,
        RopeModelBuilder::connectingModelA,
        checkExistence());
    helper.unpack(
        "connecting_model_b",
        SingleKinematicModel.class,
        RopeModelBuilder::connectingModelB,
        checkExistence());
    return helper.getBuilder().build();
  }

  public static RopeModel of(Map<String, ?> map) {
    RopeModelBuilder builder = newBuilder();
    UnpackHelper<RopeModelBuilder> helper = UnpackHelper.of(builder, map, RopeModel.class);
    helper.unpack(
        "component_a",
        String.class,
        ConnectingModel.newExtractor(map, "Rope", "component_a"),
        checkExistence());
    helper.unpack(
        "component_b",
        String.class,
        ConnectingModel.newExtractor(map, "Rope", "component_b"),
        checkExistence());
    helper.unpack("name", String.class, RopeModelBuilder::name, checkExistence());
    helper.unpack(
        "length", Double.class, RopeModelBuilder::length, checkExistence(), checkPositivity());
    helper.unpack(
        "relative_connecting_point_ax", Double.class, RopeModelBuilder::relativeConnectingPointAX);
    helper.unpack(
        "relative_connecting_point_ay", Double.class, RopeModelBuilder::relativeConnectingPointAY);
    helper.unpack(
        "relative_connecting_point_bx", Double.class, RopeModelBuilder::relativeConnectingPointBX);
    helper.unpack(
        "relative_connecting_point_by", Double.class, RopeModelBuilder::relativeConnectingPointBY);
    return helper.getBuilder().build();
  }

  public static class RopeModelBuilder implements ConnectingModelBuilder {}
}
