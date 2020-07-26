package com.hzyi.jplab.core.model.kinematic;

import static com.hzyi.jplab.core.model.Constraint.cof;
import static com.hzyi.jplab.core.model.Property.pof;
import static com.hzyi.jplab.core.util.UnpackHelper.checkExistence;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.hzyi.jplab.core.application.Application;
import com.hzyi.jplab.core.model.Assembly;
import com.hzyi.jplab.core.model.Constraint;
import com.hzyi.jplab.core.model.Property;
import com.hzyi.jplab.core.model.shape.Appearance;
import com.hzyi.jplab.core.model.shape.Catenary;
import com.hzyi.jplab.core.timeline.AlwaysPassVerifier;
import com.hzyi.jplab.core.timeline.Verifier;
import com.hzyi.jplab.core.util.Coordinate;
import com.hzyi.jplab.core.util.UnpackHelper;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

/** A RopeModel is a rigid rope. It can have pushing tensions only, and cannot have deformation. */
@EqualsAndHashCode
@Accessors(fluent = true)
@Builder(builderMethodName = "newBuilder", toBuilder = true)
@ToString
public class RopeModel extends Connector implements VerifierProvider {

  private static final double STRETCH_LOWER_BOUNDARY = 0.995;
  private static final double STRETCH_UPPER_BOUNDARY = 1.1;

  private static class StretchedVerifier implements Verifier {

    private String name;

    public StretchedVerifier(String name) {
      this.name = name;
    }

    @Override
    public boolean verify(Assembly start, Assembly finish) {
      RopeModel rope = (RopeModel) (finish.getConnector(this.name));
      return rope.impulse < 0;
    }

    @Override
    public Assembly onStart(Assembly snapshot) {
      snapshot = snapshot.copy();
      RopeModel rope = (RopeModel) (snapshot.getConnector(this.name));
      rope = rope.toBuilder().isStretched(false).build();
      snapshot.updateComponent(rope);
      return snapshot;
    }

    @Override
    public Assembly onFinish(Assembly snapshot) {
      return snapshot;
    }

    @Override
    public Verifier newVerifier() {
      // TODO: An UnstretchedVerifier should be returned, but for now the verification
      // has some flakiness when a rope goes from stretched to unstretched and thus
      // turning it off temporarily.
      return AlwaysPassVerifier.instance();
    }
  }

  private static class UnstretchedVerifier implements Verifier {

    private String name;

    public UnstretchedVerifier(String name) {
      this.name = name;
    }

    @Override
    public boolean verify(Assembly start, Assembly finish) {
      RopeModel fr = (RopeModel) (finish.getConnector(this.name));
      RopeModel sr = (RopeModel) (start.getConnector(this.name));
      return fr.distance() < STRETCH_LOWER_BOUNDARY * fr.length() || fr.distance() < sr.distance();
    }

    @Override
    public Assembly onStart(Assembly snapshot) {
      snapshot = snapshot.copy();
      RopeModel rope = (RopeModel) (snapshot.getConnector(this.name));
      rope = rope.toBuilder().isStretched(true).build();
      snapshot.updateComponent(rope);
      return snapshot;
    }

    @Override
    public Assembly onFinish(Assembly snapshot) {
      return snapshot;
    }

    @Override
    public Verifier newVerifier() {
      return new StretchedVerifier(this.name);
    }
  }

  @Getter private final String name;
  @Getter private final Connector.Type type = Connector.Type.ROPE_MODEL;

  private final double length;
  @Getter private final Coordinate relativePointU;
  @Getter private final Coordinate relativePointV;
  @Getter private final boolean isStretched;
  @Getter private final double force;
  @Getter private final double impulse;
  @Getter private final SingleKinematicModel modelU;
  @Getter private final SingleKinematicModel modelV;

  @Getter private final Catenary shape;
  @Getter private final Appearance appearance;

  // lombok doesn't override the method in parent class
  @Override
  public double length() {
    return length;
  }

  @Override
  public Table<Constraint, Property, Double> codependentMultipliers(double timeStep) {
    if (isStretched()) {
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
    return ImmutableTable.<Constraint, Property, Double>builder()
        .put(cof(this, "no-force"), pof(this, "force"), 1.0)
        .put(cof(this, "no-impulse"), pof(this, "impulse"), 1.0)
        .build();
  }

  @Override
  public List<Constraint> constraints() {
    if (isStretched()) {
      return ImmutableList.of(cof(this, "ar-upwind-balance"), cof(this, "vr-upwind-balance"));
    }
    return ImmutableList.of(cof(this, "no-force"), cof(this, "no-impulse"));
  }

  @Override
  public List<Property> properties() {
    return ImmutableList.of(pof(this, "force"), pof(this, "impulse"));
  }

  @Override
  public RopeModel merge(Map<String, ?> map) {
    UnpackHelper<RopeModelBuilder> helper = UnpackHelper.of(toBuilder(), map, RopeModel.class);
    return helper
        .unpack("model_u", SingleKinematicModel.class, RopeModelBuilder::modelU)
        .unpack("model_v", SingleKinematicModel.class, RopeModelBuilder::modelV)
        .unpack("force", Double.class, RopeModelBuilder::force)
        .unpack("impulse", Double.class, RopeModelBuilder::impulse)
        .getBuilder()
        .build();
  }

  @Override
  public List<Verifier> verifiers() {
    return ImmutableList.of(
        isStretched ? new StretchedVerifier(this.name) : new UnstretchedVerifier(this.name));
  }

  public static RopeModel of(Map<String, ?> map) {
    RopeModelBuilder builder = newBuilder();
    UnpackHelper<RopeModelBuilder> helper = UnpackHelper.of(builder, map, RopeModel.class);

    helper.unpack("name", String.class, RopeModelBuilder::name, checkExistence());
    helper.unpack(
        "model_u",
        String.class,
        Connector.connectedModelExtractor(map, "Rope", "model_u"),
        checkExistence());
    helper.unpack(
        "model_v",
        String.class,
        Connector.connectedModelExtractor(map, "Rope", "model_v"),
        checkExistence());
    helper.unpack(
        "relative_point_ux",
        Double.class,
        "relative_point_uy",
        Double.class,
        Connector.coordinateExtractor(RopeModelBuilder::relativePointU));
    helper.unpack(
        "relative_point_vx",
        Double.class,
        "relative_point_vy",
        Double.class,
        Connector.coordinateExtractor(RopeModelBuilder::relativePointV));
    helper.unpack("length", Double.class, RopeModelBuilder::length, checkExistence());
    helper.unpack("is_stretched", Boolean.class, RopeModelBuilder::isStretched);
    return helper.getBuilder().build();
  }

  public void paint() {

    Application.getPainterFactory()
        .getCatenaryPainter()
        .paint(relativePointU, relativePointV, name, length, isStretched, appearance);
  }

  private double distance() {
    return super.length();
  }

  private static double impulse(SingleKinematicModel model, double timeStep) {
    if (model.isRigidBody()) {
      return 1.0 / ((RigidBody) model).mass() * timeStep;
    }
    return 0;
  }

  public static class RopeModelBuilder implements ConnectorBuilder {}
}
