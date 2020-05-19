package com.hzyi.jplab.core.model.kinematic;

import static com.hzyi.jplab.core.util.UnpackHelper.checkExistence;
import static com.hzyi.jplab.core.util.UnpackHelper.checkPositivity;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.hzyi.jplab.core.model.Property;
import com.hzyi.jplab.core.util.Coordinate;
import com.hzyi.jplab.core.util.UnpackHelper;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
    SpringConnectorBuilder builder = toBuilder();
    UnpackHelper<SpringConnectorBuilder> helper = UnpackHelper.of(builder, map, SpringModel.class);
    helper.unpack(
        "model_u", SingleKinematicModel.class, SpringConnectorBuilder::modelU, checkExistence());
    helper.unpack(
        "model_v", SingleKinematicModel.class, SpringConnectorBuilder::modelV, checkExistence());
    return helper.getBuilder().build();
  }

  public static SpringModel of(Map<String, ?> map) {
    SpringConnectorBuilder builder = newBuilder();
    UnpackHelper<SpringConnectorBuilder> helper = UnpackHelper.of(builder, map, SpringModel.class);
    helper.unpack(
        "model_u",
        String.class,
        Connector.connectedModelExtractor(map, "Spring", "model_u"),
        checkExistence());
    helper.unpack(
        "model_v",
        String.class,
        Connector.connectedModelExtractor(map, "Spring", "model_v"),
        checkExistence());
    helper.unpack("name", String.class, SpringConnectorBuilder::name, checkExistence());
    helper.unpack(
        "unstretched_length",
        Double.class,
        SpringConnectorBuilder::unstretchedLength,
        checkExistence(),
        checkPositivity());
    helper.unpack(
        "stiffness",
        Double.class,
        SpringConnectorBuilder::stiffness,
        checkExistence(),
        checkPositivity());
    helper.unpack("relative_point_ux", Double.class, SpringConnectorBuilder::relativePointUX);
    helper.unpack("relative_point_uy", Double.class, SpringConnectorBuilder::relativePointUY);
    helper.unpack("relative_point_vx", Double.class, SpringConnectorBuilder::relativePointVX);
    helper.unpack("relative_point_vy", Double.class, SpringConnectorBuilder::relativePointVY);
    return helper.getBuilder().build();
  }

  @Override
  public List<String> codependentProperties() {
    return Collections.emptyList();
  }

  @Override
  public Table<String, String, Double> codependentMultipliers(double timeStep) {
    String aax = Property.format(modelU, "ax");
    String aay = Property.format(modelU, "ay");
    String bax = Property.format(modelV, "ax");
    String bay = Property.format(modelV, "ay");
    return ImmutableTable.<String, String, Double>builder()
        .put(aax, Property.constant(), force() * Math.cos(theta()))
        .put(aay, Property.constant(), force() * Math.sin(theta()))
        .put(bax, Property.constant(), -force() * Math.cos(theta()))
        .put(bay, Property.constant(), -force() * Math.sin(theta()))
        // support rotation
        .build();
  }

  private double force() {
    return (length() - unstretchedLength) * stiffness;
  }

  public static class SpringConnectorBuilder implements Connector.ConnectorBuilder {}
}
