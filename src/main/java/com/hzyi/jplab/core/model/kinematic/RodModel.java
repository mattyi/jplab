package com.hzyi.jplab.core.model.kinematic;

import static com.hzyi.jplab.core.model.Constraint.cof;
import static com.hzyi.jplab.core.model.Property.pof;
import static com.hzyi.jplab.core.util.UnpackHelper.checkExistence;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.hzyi.jplab.core.application.Application;
import com.hzyi.jplab.core.application.config.ApplicationConfig;
import com.hzyi.jplab.core.model.Constraint;
import com.hzyi.jplab.core.model.Property;
import com.hzyi.jplab.core.model.shape.Appearance;
import com.hzyi.jplab.core.model.shape.Line;
import com.hzyi.jplab.core.util.Coordinate;
import com.hzyi.jplab.core.util.UnpackHelper;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

/** A RodModel is a rigid rod. It can have push and pull tensions, but cannot have deformation. */
@ToString
@EqualsAndHashCode
@Accessors(fluent = true)
@Builder(builderMethodName = "newBuilder", toBuilder = true)
public class RodModel extends Connector {

  @Getter private final String name;
  @Getter private final Connector.Type type = Connector.Type.ROD_MODEL;

  @Getter @Builder.Default private final Coordinate relativePointU = new Coordinate(0, 0);
  @Getter @Builder.Default private final Coordinate relativePointV = new Coordinate(0, 0);
  @Getter private final double force;
  @Getter private SingleKinematicModel modelU;
  @Getter private SingleKinematicModel modelV;

  @Getter private final Line shape;
  @Getter private final Appearance appearance;

  @Override
  public Table<Constraint, Property, Double> codependentMultipliers(double timeStep) {
    return ImmutableTable.<Constraint, Property, Double>builder()
        .put(
            cof(modelU, "vx-upwind"),
            pof(this, "impulse"),
            impulse(modelU, timeStep) * Math.cos(theta()))
        .put(
            cof(modelU, "vy-upwind"),
            pof(this, "impulse"),
            impulse(modelU, timeStep) * Math.sin(theta()))
        .put(
            cof(modelV, "vx-upwind"),
            pof(this, "impulse"),
            impulse(modelV, timeStep) * -Math.cos(theta()))
        .put(
            cof(modelV, "vy-upwind"),
            pof(this, "impulse"),
            impulse(modelV, timeStep) * -Math.sin(theta()))
        .put(cof(this, "vr-upwind-balance"), pof(modelU, "vx"), Math.cos(theta()))
        .put(cof(this, "vr-upwind-balance"), pof(modelU, "vy"), Math.sin(theta()))
        .put(cof(this, "vr-upwind-balance"), pof(modelV, "vx"), -Math.cos(theta()))
        .put(cof(this, "vr-upwind-balance"), pof(modelV, "vy"), -Math.sin(theta()))
        .put(cof(modelU, "ax-upwind-balance"), pof(this, "force"), Math.cos(theta()))
        .put(cof(modelU, "ay-upwind-balance"), pof(this, "force"), Math.sin(theta()))
        .put(cof(modelV, "ax-upwind-balance"), pof(this, "force"), -Math.cos(theta()))
        .put(cof(modelV, "ay-upwind-balance"), pof(this, "force"), -Math.sin(theta()))
        .put(cof(this, "ar-upwind-balance"), pof(modelU, "ax"), Math.cos(theta()))
        .put(cof(this, "ar-upwind-balance"), pof(modelU, "ay"), Math.sin(theta()))
        .put(cof(this, "ar-upwind-balance"), pof(modelV, "ax"), -Math.cos(theta()))
        .put(cof(this, "ar-upwind-balance"), pof(modelV, "ay"), -Math.sin(theta()))
        .build();
  }

  @Override
  public List<Constraint> constraints() {
    return ImmutableList.of(cof(this, "ar-upwind-balance"), cof(this, "vr-upwind-balance"));
  }

  @Override
  public List<Property> properties() {
    return ImmutableList.of(pof(this, "impulse"), pof(this, "force"));
  }

  @Override
  public RodModel merge(Map<String, ?> map) {
    RodModelBuilder builder = toBuilder();
    UnpackHelper<RodModelBuilder> helper = UnpackHelper.of(builder, map, RodModel.class);
    helper.unpack("model_u", SingleKinematicModel.class, RodModelBuilder::modelU);
    helper.unpack("model_v", SingleKinematicModel.class, RodModelBuilder::modelV);
    helper.unpack("force", Double.class, RodModelBuilder::force);
    return helper.getBuilder().build();
  }

  public static RodModel of(ApplicationConfig.ConnectorConfig config) {
    RodModelBuilder builder = newBuilder();
    builder.name(config.getName());
    Map<String, Object> specs = config.getConnectorSpecs();
    UnpackHelper<RodModelBuilder> helper = UnpackHelper.of(builder, specs, RodModel.class);
    BiFunction<RodModelBuilder, String, RodModelBuilder> collectorU =
        Connector.connectedModelExtractor(specs, "Rod", "model_u");
    BiFunction<RodModelBuilder, String, RodModelBuilder> collectorV =
        Connector.connectedModelExtractor(specs, "Rod", "model_v");

    builder.name(config.getName());
    helper.unpack("model_u", String.class, collectorU, checkExistence());
    helper.unpack("model_v", String.class, collectorV, checkExistence());

    helper.unpack(
        "relative_point_ux",
        Double.class,
        "relative_point_uy",
        Double.class,
        Connector.coordinateExtractor(RodModelBuilder::relativePointU));
    helper.unpack(
        "relative_point_vx",
        Double.class,
        "relative_point_vy",
        Double.class,
        Connector.coordinateExtractor(RodModelBuilder::relativePointV));

    Line shape = Line.of(specs);
    Appearance appearance = Appearance.of(config.getAppearance());

    RodModel rod = helper.getBuilder().shape(shape).appearance(appearance).build();
    return rod;
  }

  @Override
  public void paint() {
    Application.getPainterFactory().getLinePainter().paint(pointU(), pointV(), shape, appearance);
  }

  private static double impulse(SingleKinematicModel model, double timeStep) {
    if (model.isRigidBody()) {
      return 1.0 / ((RigidBody) model).mass() * timeStep;
    }
    return 0;
  }

  public static class RodModelBuilder implements ConnectorBuilder {}
}
