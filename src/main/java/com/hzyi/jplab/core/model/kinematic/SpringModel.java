package com.hzyi.jplab.core.model.kinematic;

import static com.hzyi.jplab.core.model.Constraint.cof;
import static com.hzyi.jplab.core.model.Property.constant;
import static com.hzyi.jplab.core.util.UnpackHelper.checkExistence;
import static com.hzyi.jplab.core.util.UnpackHelper.checkPositivity;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.hzyi.jplab.core.application.Application;
import com.hzyi.jplab.core.model.Constraint;
import com.hzyi.jplab.core.model.Property;
import com.hzyi.jplab.core.model.shape.Appearance;
import com.hzyi.jplab.core.model.shape.ZigzagLine;
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

/**
 * A SpringModel is a spring. It can have both pushing and pulling tensions, and the magnitude of
 * the tension is propotional to its deformation.
 */
@EqualsAndHashCode
@ToString
@Accessors(fluent = true)
@Builder(builderMethodName = "newBuilder", toBuilder = true)
public class SpringModel extends Connector {

  @Getter private final String name;
  @Getter private final Connector.Type type = Connector.Type.SPRING_MODEL;
  @Getter private final double stiffness;
  @Getter private final double unstretchedLength;
  @Getter @Builder.Default private final Coordinate relativePointU = new Coordinate(0, 0);
  @Getter @Builder.Default private final Coordinate relativePointV = new Coordinate(0, 0);
  @Getter private final SingleKinematicModel modelU;
  @Getter private final SingleKinematicModel modelV;
  @Getter private final ZigzagLine shape;
  @Getter private final Appearance appearance;

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
    helper.unpack("unstretched_length", Double.class, checkExistence(), checkPositivity());
    helper.unpack("stiffness", SpringModelBuilder::stiffness, checkExistence(), checkPositivity());

    double ux = MoreObjects.firstNonNull(helper.unpack("relative_point_ux", Double.class), 0.0);
    double uy = MoreObjects.firstNonNull(helper.unpack("relative_point_uy", Double.class), 0.0);
    double vx = MoreObjects.firstNonNull(helper.unpack("relative_point_ux", Double.class), 0.0);
    double vy = MoreObjects.firstNonNull(helper.unpack("relative_point_uy", Double.class), 0.0);

    ZigzagLine shape = ZigzagLine.of(map);
    Appearance appearance = Appearance.of(map);

    return helper
        .getBuilder()
        .relativePointU(new Coordinate(ux, uy))
        .relativePointV(new Coordinate(vx, vy))
        .shape(shape)
        .appearance(appearance)
        .build();
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

  @Override
  public void paint() {
    Application.getPainterFactory()
        .getZigzagLinePainter()
        .paint(pointU(), pointV(), theta(), shape, appearance);
  }

  public static class SpringModelBuilder implements Connector.ConnectorBuilder {}
}
