package com.hzyi.jplab.core.model.kinematic;

import static com.hzyi.jplab.core.util.UnpackHelper.checkExistence;

import com.google.common.collect.Table;
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
public class RopeModel extends Connector {

  @Getter private String name;
  @Getter private double length;
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
    throw new UnsupportedOperationException("not needed yet");
  }

  @Override
  public List<String> codependentProperties() {
    throw new UnsupportedOperationException("not needed yet");
  }

  @Override
  public RopeModel merge(Map<String, ?> map) {
    RopeModelBuilder builder = toBuilder();
    UnpackHelper<RopeModelBuilder> helper = UnpackHelper.of(builder, map, RopeModel.class);
    helper.unpack("model_u", SingleKinematicModel.class, RopeModelBuilder::modelU);
    helper.unpack("model_v", SingleKinematicModel.class, RopeModelBuilder::modelV);
    return helper.getBuilder().build();
  }

  public static RopeModel of(Map<String, ?> map) {
    RopeModelBuilder builder = newBuilder();
    UnpackHelper<RopeModelBuilder> helper = UnpackHelper.of(builder, map, RopeModel.class);
    BiFunction<RopeModelBuilder, String, RopeModelBuilder> extractorU =
        Connector.connectedModelExtractor(map, "Rope", "model_u");
    BiFunction<RopeModelBuilder, String, RopeModelBuilder> extractorV =
        Connector.connectedModelExtractor(map, "Rope", "model_u");
    helper.unpack("model_u", String.class, extractorU, checkExistence());
    helper.unpack("model_v", String.class, extractorV, checkExistence());
    helper.unpack("name", String.class, RopeModelBuilder::name, checkExistence());
    helper.unpack("relative_point_ux", Double.class, RopeModelBuilder::relativePointUX);
    helper.unpack("relative_point_uy", Double.class, RopeModelBuilder::relativePointUY);
    helper.unpack("relative_point_vx", Double.class, RopeModelBuilder::relativePointVX);
    helper.unpack("relative_point_vy", Double.class, RopeModelBuilder::relativePointVY);
    return helper.getBuilder().build();
  }

  public static class RopeModelBuilder implements modelBuilder {}
}
