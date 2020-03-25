package com.hzyi.jplab.core.model.kinematic;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.hzyi.jplab.core.util.Coordinate;
import com.hzyi.jplab.core.util.CoordinateSystem;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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

  public double force() {
    return (length() - originalLength) * stiffness;
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
  public SpringModel unpack(Map<String, ?> map) {
    return toBuilder()
        .connectingModelA((SingleKinematicModel) map.get("connecting_model_a"))
        .connectingModelB((SingleKinematicModel) map.get("connecting_model_b"))
        .build();
  }

  @Override
  public List<String> codependentFields() {
    return Collections.emptyList();
  }

  @Override
  public Table<String, String, Double> codependentMultipliers() {
    String axConnectingA = getFieldFullName(connectingModelA, "ax");
    String ayConnectingA = getFieldFullName(connectingModelA, "ay");
    String axConnectingB = getFieldFullName(connectingModelB, "ax");
    String ayConnectingB = getFieldFullName(connectingModelB, "ay");
    return ImmutableTable.<String, String, Double>builder()
        .put(axConnectingA, getConstantFieldName(), force() * Math.cos(theta()))
        .put(ayConnectingA, getConstantFieldName(), force() * Math.sin(theta()))
        .put(axConnectingB, getConstantFieldName(), -force() * Math.cos(theta()))
        .put(ayConnectingB, getConstantFieldName(), -force() * Math.sin(theta()))
        // support rotation
        .build();
  }
}
