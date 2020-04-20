package com.hzyi.jplab.core.model.kinematic;

import static com.hzyi.jplab.core.util.UnpackHelper.checkExistence;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.hzyi.jplab.core.util.UnpackHelper;
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
public class StaticModel extends SingleKinematicModel {

  @Getter private String name;
  @Getter private final double x;
  @Getter private final double y;
  @Getter private final double theta;

  @Override
  public final KinematicModel.Type type() {
    return KinematicModel.Type.STATIC_MODEL;
  }

  @Override
  public final double vx() {
    return 0;
  }

  @Override
  public final double vy() {
    return 0;
  }

  @Override
  public final double omega() {
    return 0;
  }

  @Override
  public final double ax() {
    return 0;
  }

  @Override
  public final double ay() {
    return 0;
  }

  @Override
  public final double alpha() {
    return 0;
  }

  @Override
  public StaticModel merge(Map<String, ?> map) {
    return this;
  }

  public static StaticModel of(Map<String, ?> map) {
    StaticModelBuilder builder = newBuilder();
    UnpackHelper<StaticModelBuilder> helper = UnpackHelper.of(builder, map, MassPoint.class);
    helper.unpack("x", Double.class, StaticModelBuilder::x);
    helper.unpack("y", Double.class, StaticModelBuilder::y);
    helper.unpack("theta", Double.class, StaticModelBuilder::theta);
    helper.unpack("name", String.class, StaticModelBuilder::name, checkExistence());
    return helper.getBuilder().build();
  }

  @Override
  public List<String> codependentFields() {
    return Collections.emptyList();
  }

  @Override
  public Table<String, String, Double> codependentMultipliers() {
    return ImmutableTable.of();
  }
}
