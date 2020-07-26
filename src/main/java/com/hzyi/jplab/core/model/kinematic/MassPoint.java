package com.hzyi.jplab.core.model.kinematic;

import static com.hzyi.jplab.core.model.Constraint.cof;
import static com.hzyi.jplab.core.model.Property.constant;
import static com.hzyi.jplab.core.model.Property.pof;
import static com.hzyi.jplab.core.util.UnpackHelper.checkExistence;
import static com.hzyi.jplab.core.util.UnpackHelper.checkPositivity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.hzyi.jplab.core.application.Application;
import com.hzyi.jplab.core.model.Constraint;
import com.hzyi.jplab.core.model.Property;
import com.hzyi.jplab.core.model.shape.Appearance;
import com.hzyi.jplab.core.model.shape.Circle;
import com.hzyi.jplab.core.util.Coordinate;
import com.hzyi.jplab.core.util.UnpackHelper;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * A MassPoint is a component that has mass but not moment of inertia. A MassPoint can move
 * horizontally or vertically, but cannot rotate.
 */
@EqualsAndHashCode
@ToString
@Accessors(fluent = true)
@Builder(builderMethodName = "newBuilder", toBuilder = true)
public class MassPoint extends RigidBody {

  @Getter private final String name;
  @Getter private final SingleKinematicModel.Type type = SingleKinematicModel.Type.MASS_POINT;

  @Getter private final double x;
  @Getter private final double y;
  @Getter private final double vx;
  @Getter private final double vy;
  @Getter private final double ax;
  @Getter private final double ay;
  @Getter private final double mass;
  @Getter private final double theta = 0.0;
  @Getter private final double omega = 0.0;
  @Getter private final double alpha = 0.0;
  @Getter private final double momentOfInertia = Double.POSITIVE_INFINITY;

  @Getter private final Circle shape;
  @Getter private final Appearance appearance;

  @Override
  public MassPoint merge(Map<String, ?> map) {
    MassPointBuilder builder = toBuilder();
    UnpackHelper<MassPointBuilder> helper = UnpackHelper.of(builder, map, MassPoint.class);
    helper.unpack("x", MassPointBuilder::x);
    helper.unpack("y", MassPointBuilder::y);
    helper.unpack("vx", MassPointBuilder::vx);
    helper.unpack("vy", MassPointBuilder::vy);
    helper.unpack("ax", MassPointBuilder::ax);
    helper.unpack("ay", MassPointBuilder::ay);
    return helper.getBuilder().build();
  }

  public static MassPoint of(Map<String, ?> map) {
    MassPointBuilder builder = newBuilder();
    UnpackHelper<MassPointBuilder> h = UnpackHelper.of(builder, map, MassPoint.class);
    h.unpack("name", String.class, MassPointBuilder::name, checkExistence());

    h.unpack("mass", MassPointBuilder::mass, checkExistence(), checkPositivity());
    h.unpack("x", MassPointBuilder::x);
    h.unpack("y", MassPointBuilder::y);
    h.unpack("vx", MassPointBuilder::vx);
    h.unpack("vy", MassPointBuilder::vy);
    h.unpack("ax", MassPointBuilder::ax);
    h.unpack("ay", MassPointBuilder::ay);

    // TODO: supporting more shapes
    Circle shape = Circle.of(map);
    Appearance appearance = Appearance.of(map);

    return h.getBuilder().shape(shape).appearance(appearance).build();
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

  @Override
  public void paint() {
    Application.getPainterFactory()
        .getCirclePainter()
        .paint(new Coordinate(x, y), shape, appearance);
  }
}
