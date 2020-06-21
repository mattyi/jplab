package com.hzyi.jplab.core.model.kinematic;

import static com.hzyi.jplab.core.model.Constraint.cof;
import static com.hzyi.jplab.core.model.Property.constant;
import static com.hzyi.jplab.core.util.UnpackHelper.checkExistence;
import static com.hzyi.jplab.core.util.UnpackHelper.checkPositivity;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.hzyi.jplab.core.model.Constraint;
import com.hzyi.jplab.core.model.Property;
import com.hzyi.jplab.core.util.Coordinate;
import com.hzyi.jplab.core.util.UnpackHelper;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

@EqualsAndHashCode
@ToString
@Accessors(fluent = true)
@Builder(builderMethodName = "newBuilder", toBuilder = true)
public class SpringModel extends Connector {

  @Getter private String name;
  @Getter private double stiffness;
  @Getter private double unstretchedLength;
  private double relativePointUX;
  private double relativePointUY;
  private double relativePointVX;
  private double relativePointVY;
  @Getter private SingleKinematicModel modelU;
  @Getter private SingleKinematicModel modelV;

  public final KinematicModel.Type type() {
    return KinematicModel.Type.SPRING_MODEL;
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
  public SpringModel merge(Map<String, ?> map) {
    SpringModelBuilder builder = toBuilder();
    UnpackHelper<SpringModelBuilder> h = UnpackHelper.of(builder, map, SpringModel.class);
    h.unpack("model_u", SingleKinematicModel.class, SpringModelBuilder::modelU, checkExistence());
    h.unpack("model_v", SingleKinematicModel.class, SpringModelBuilder::modelV, checkExistence());
    return h.getBuilder().build();
  }

  public static SpringModel of(Map<String, ?> map) {
    SpringModelBuilder builder = newBuilder();
    UnpackHelper<SpringModelBuilder> helper = UnpackHelper.of(builder, map, SpringModel.class);
    BiFunction<SpringModelBuilder, String, SpringModelBuilder> collectorU =
        Connector.connectedModelExtractor(map, "Spring", "model_u");
    BiFunction<SpringModelBuilder, String, SpringModelBuilder> collectorV =
        Connector.connectedModelExtractor(map, "Spring", "model_v");
    helper.unpack("model_u", String.class, collectorU, checkExistence());
    helper.unpack("model_v", String.class, collectorV, checkExistence());
    helper.unpack("name", String.class, SpringModelBuilder::name, checkExistence());
    helper.unpack(
        "unstretched_length",
        Double.class,
        SpringModelBuilder::unstretchedLength,
        checkExistence(),
        checkPositivity());
    helper.unpack(
        "stiffness",
        Double.class,
        SpringModelBuilder::stiffness,
        checkExistence(),
        checkPositivity());
    helper.unpack("relative_point_ux", Double.class, SpringModelBuilder::relativePointUX);
    helper.unpack("relative_point_uy", Double.class, SpringModelBuilder::relativePointUY);
    helper.unpack("relative_point_vx", Double.class, SpringModelBuilder::relativePointVX);
    helper.unpack("relative_point_vy", Double.class, SpringModelBuilder::relativePointVY);
    return helper.getBuilder().build();
  }

  @Override
  public List<Constraint> constraints() {
    return Collections.emptyList();
  }

  @Override
  public List<Property> properties() {
    return Collections.emptyList();
  }

  @Override
  public Table<Constraint, Property, Double> codependentMultipliers(double timeStep) {
    return ImmutableTable.<Constraint, Property, Double>builder()
        .put(cof(modelU, "ax-upwind-balance"), constant(), force() * Math.cos(theta()))
        .put(cof(modelU, "ay-upwind-balance"), constant(), force() * Math.sin(theta()))
        .put(cof(modelV, "ax-upwind-balance"), constant(), -force() * Math.cos(theta()))
        .put(cof(modelV, "ay-upwind-balance"), constant(), -force() * Math.sin(theta()))
        // support rotation
        .build();
  }

  @Override
  public double force() {
    return (length() - unstretchedLength) * stiffness;
  }

  public static class SpringModelBuilder implements Connector.ConnectorBuilder {}
}
