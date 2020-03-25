package com.hzyi.jplab.core.model.kinematic;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

@EqualsAndHashCode
@Accessors(fluent = true)
@Builder(builderMethodName = "newBuilder", toBuilder = true)
public class MassPoint extends RigidBody {

  @Getter private String name;
  @Getter private double x;
  @Getter private double y;
  @Getter private double vx;
  @Getter private double vy;
  @Getter private double ax;
  @Getter private double ay;
  @Getter private double mass;

  @Override
  public final double theta() {
    return 0;
  }

  @Override
  public final double omega() {
    return 0;
  }

  @Override
  public final double alpha() {
    return 0;
  }

  @Override
  public final double momentOfInertia() {
    return Double.POSITIVE_INFINITY;
  }

  @Override
  public MassPoint unpack(Map<String, ?> map) {
    return toBuilder()
        .x(MoreObjects.firstNonNull((Double) map.get("x"), x()))
        .y(MoreObjects.firstNonNull((Double) map.get("y"), y()))
        .vx(MoreObjects.firstNonNull((Double) map.get("vx"), vx()))
        .vy(MoreObjects.firstNonNull((Double) map.get("vy"), vy()))
        .ax(MoreObjects.firstNonNull((Double) map.get("ax"), ax()))
        .ay(MoreObjects.firstNonNull((Double) map.get("ay"), ay()))
        .build();
  }

  @Override
  public List<String> codependentFields() {
    return ImmutableList.of(getFieldFullName("ax"), getFieldFullName("ay"));
  }

  @Override
  public Table<String, String, Double> codependentMultipliers() {
    return ImmutableTable.<String, String, Double>builder()
        .put(getFieldFullName("ax"), getFieldFullName("ax"), -mass)
        .put(getFieldFullName("ay"), getFieldFullName("ay"), -mass)
        .build();
  }
}
