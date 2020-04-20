package com.hzyi.jplab.core.model.kinematic;

import static com.hzyi.jplab.core.util.UnpackHelper.checkExistence;
import static com.hzyi.jplab.core.util.UnpackHelper.checkPositivity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
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
  public final KinematicModel.Type type() {
    return KinematicModel.Type.MASS_POINT;
  }

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
  public MassPoint merge(Map<String, ?> map) {
    MassPointBuilder builder = toBuilder();
    UnpackHelper<MassPointBuilder> helper = UnpackHelper.of(builder, map, MassPoint.class);
    helper.unpack("x", Double.class, MassPointBuilder::x);
    helper.unpack("y", Double.class, MassPointBuilder::y);
    helper.unpack("vx", Double.class, MassPointBuilder::vx);
    helper.unpack("vy", Double.class, MassPointBuilder::vy);
    helper.unpack("ax", Double.class, MassPointBuilder::ax);
    helper.unpack("ay", Double.class, MassPointBuilder::ay);
    return helper.getBuilder().build();
  }

  public static MassPoint of(Map<String, ?> map) {
    MassPointBuilder builder = newBuilder();
    UnpackHelper<MassPointBuilder> helper = UnpackHelper.of(builder, map, MassPoint.class);
    helper.unpack("x", Double.class, MassPointBuilder::x);
    helper.unpack("y", Double.class, MassPointBuilder::y);
    helper.unpack("vx", Double.class, MassPointBuilder::vx);
    helper.unpack("vy", Double.class, MassPointBuilder::vy);
    helper.unpack("ax", Double.class, MassPointBuilder::ax);
    helper.unpack("ay", Double.class, MassPointBuilder::ay);
    helper.unpack(
        "mass", Double.class, MassPointBuilder::mass, checkExistence(), checkPositivity());
    helper.unpack("name", String.class, MassPointBuilder::name, checkExistence());
    return helper.getBuilder().build();
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
