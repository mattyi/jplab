package com.hzyi.jplab.core.model.kinematic;

import static com.hzyi.jplab.core.util.UnpackHelper.checkExistence;
import static com.hzyi.jplab.core.util.UnpackHelper.checkPositivity;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.hzyi.jplab.core.application.exceptions.Prechecks;
import com.hzyi.jplab.core.model.AssemblySnapshot;
import com.hzyi.jplab.core.model.Field;
import com.hzyi.jplab.core.util.Coordinate;
import com.hzyi.jplab.core.util.CoordinateSystem;
import com.hzyi.jplab.core.util.UnpackHelper;
import java.util.Collections;
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
public class SpringModel extends ConnectingModel {

  @Getter private String name;
  @Getter private double stiffness;
  @Getter private double originalLength;
  private double relativeConnectingPointAX;
  private double relativeConnectingPointAY;
  private double relativeConnectingPointBX;
  private double relativeConnectingPointBY;
  @Getter private SingleKinematicModel connectingModelA;
  @Getter private SingleKinematicModel connectingModelB;

  public final KinematicModel.Type type() {
    return KinematicModel.Type.SPRING_MODEL;
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
  public SpringModel merge(Map<String, ?> map) {
    SpringModelBuilder builder = toBuilder();
    UnpackHelper<SpringModelBuilder> helper = UnpackHelper.of(builder, map, SpringModel.class);
    helper.unpack(
        "connecting_model_a",
        SingleKinematicModel.class,
        SpringModelBuilder::connectingModelA,
        checkExistence());
    helper.unpack(
        "connecting_model_b",
        SingleKinematicModel.class,
        SpringModelBuilder::connectingModelB,
        checkExistence());
    return helper.getBuilder().build();
  }

  public static SpringModel of(Map<String, ?> map) {
    SpringModelBuilder builder = newBuilder();
    UnpackHelper<SpringModelBuilder> helper = UnpackHelper.of(builder, map, SpringModel.class);

    helper.unpack(
        "component_a", String.class, componentExtractor(map, "component_a"), checkExistence());
    helper.unpack(
        "component_b", String.class, componentExtractor(map, "component_b"), checkExistence());
    helper.unpack("name", String.class, SpringModelBuilder::name, checkExistence());
    helper.unpack(
        "original_length",
        Double.class,
        SpringModelBuilder::originalLength,
        checkExistence(),
        checkPositivity());
    helper.unpack(
        "stiffness",
        Double.class,
        SpringModelBuilder::stiffness,
        checkExistence(),
        checkPositivity());
    helper.unpack(
        "relative_connecting_point_ax",
        Double.class,
        SpringModelBuilder::relativeConnectingPointAX);
    helper.unpack(
        "relative_connecting_point_ay",
        Double.class,
        SpringModelBuilder::relativeConnectingPointAY);
    helper.unpack(
        "relative_connecting_point_bx",
        Double.class,
        SpringModelBuilder::relativeConnectingPointBX);
    helper.unpack(
        "relative_connecting_point_by",
        Double.class,
        SpringModelBuilder::relativeConnectingPointBY);

    return helper.getBuilder().build();
  }

  @Override
  public List<String> codependentFields() {
    return Collections.emptyList();
  }

  @Override
  public Table<String, String, Double> codependentMultipliers(double timeStep) {
    String aax = Field.format(connectingModelA, "ax");
    String aay = Field.format(connectingModelA, "ay");
    String bax = Field.format(connectingModelB, "ax");
    String bay = Field.format(connectingModelB, "ay");
    return ImmutableTable.<String, String, Double>builder()
        .put(aax, Field.constant(), force() * Math.cos(theta()))
        .put(aay, Field.constant(), force() * Math.sin(theta()))
        .put(bax, Field.constant(), -force() * Math.cos(theta()))
        .put(bay, Field.constant(), -force() * Math.sin(theta()))
        // support rotation
        .build();
  }

  private double force() {
    return (length() - originalLength) * stiffness;
  }

  private static BiFunction<SpringModelBuilder, String, SpringModelBuilder> componentExtractor(
      Map<String, ?> map, final String field) {
    final AssemblySnapshot snapshot = Prechecks.checkFieldExists(map, "", "_assembly_snapshot");
    BiFunction<SpringModelBuilder, String, SpringModelBuilder> extractor =
        new BiFunction<SpringModelBuilder, String, SpringModelBuilder>() {
          @Override
          public SpringModelBuilder apply(SpringModelBuilder builder, String component) {
            KinematicModel model = snapshot.get(component);
            Prechecks.checkFieldExists(model, "Spring", component);
            Prechecks.checkFieldValue(
                model instanceof SingleKinematicModel,
                "Spring",
                component,
                "component %s is not a single kinematic model",
                component);

            if (field.equals("component_a")) {
              builder.connectingModelA((SingleKinematicModel) model);
            } else if (field.equals("component_b")) {
              builder.connectingModelB((SingleKinematicModel) model);
            } else {
              throw new IllegalStateException("internal: unknown field " + field);
            }
            return builder;
          }
        };
    return extractor;
  }
}
