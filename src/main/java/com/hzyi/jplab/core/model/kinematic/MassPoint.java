package com.hzyi.jplab.core.model.kinematic;

import static com.hzyi.jplab.core.model.Constraint.cof;
import static com.hzyi.jplab.core.model.Property.constant;
import static com.hzyi.jplab.core.model.Property.pof;
import static com.hzyi.jplab.core.util.UnpackHelper.checkExistence;
import static com.hzyi.jplab.core.util.UnpackHelper.checkPositivity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.hzyi.jplab.core.model.Constraint;
import com.hzyi.jplab.core.model.Property;
import com.hzyi.jplab.core.util.UnpackHelper;
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
    UnpackHelper<MassPointBuilder> h = UnpackHelper.of(builder, map, MassPoint.class);
    h.unpack("mass", Double.class, MassPointBuilder::mass, checkExistence(), checkPositivity());
    h.unpack("name", String.class, MassPointBuilder::name, checkExistence());
    h.unpack("x", Double.class, MassPointBuilder::x);
    h.unpack("y", Double.class, MassPointBuilder::y);
    h.unpack("vx", Double.class, MassPointBuilder::vx);
    h.unpack("vy", Double.class, MassPointBuilder::vy);
    h.unpack("ax", Double.class, MassPointBuilder::ax);
    h.unpack("ay", Double.class, MassPointBuilder::ay);
    return h.getBuilder().build();
  }

  @Override
  public List<Constraint> constraints() {
    return ImmutableList.of(
        cof(this, "x-upwind"),
        cof(this, "y-upwind"),
        cof(this, "vx-upwind"),
        cof(this, "vy-upwind"),
        cof(this, "ax-upwind-balance"),
        cof(this, "ay-upwind-balance"));
  }

  @Override
  public List<Property> properties() {
    return ImmutableList.of(
        pof(this, "x"),
        pof(this, "y"),
        pof(this, "vx"),
        pof(this, "vy"),
        pof(this, "ax"),
        pof(this, "ay"));
  }

  @Override
  public Table<Constraint, Property, Double> codependentMultipliers(double timeStep) {
    Table<Constraint, Property, Double> answer =
        ImmutableTable.<Constraint, Property, Double>builder()
            .put(cof(this, "x-upwind"), pof(this, "x"), 1.0)
            .put(cof(this, "y-upwind"), pof(this, "y"), 1.0)
            .put(cof(this, "vx-upwind"), pof(this, "vx"), 1.0)
            .put(cof(this, "vy-upwind"), pof(this, "vy"), 1.0)
            .put(cof(this, "ax-upwind-balance"), pof(this, "ax"), -mass)
            .put(cof(this, "ay-upwind-balance"), pof(this, "ay"), -mass)
            .put(cof(this, "x-upwind"), constant(), -x() - vx() * timeStep)
            .put(cof(this, "y-upwind"), constant(), -y() - vy() * timeStep)
            .put(cof(this, "vx-upwind"), constant(), -vx() - ax() * timeStep)
            .put(cof(this, "vy-upwind"), constant(), -vy() - ay() * timeStep)
            .build();
    return answer;
  }
}
