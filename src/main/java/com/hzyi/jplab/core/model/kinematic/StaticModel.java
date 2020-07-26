package com.hzyi.jplab.core.model.kinematic;

import static com.hzyi.jplab.core.util.UnpackHelper.checkExistence;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.hzyi.jplab.core.application.Application;
import com.hzyi.jplab.core.model.Constraint;
import com.hzyi.jplab.core.model.Property;
import com.hzyi.jplab.core.model.shape.Appearance;
import com.hzyi.jplab.core.model.shape.Edge;
import com.hzyi.jplab.core.util.UnpackHelper;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

/** A StaticModel is a kinematic model that is fixed to a point. */
@ToString
@EqualsAndHashCode
@Accessors(fluent = true)
@Builder(builderMethodName = "newBuilder", toBuilder = true)
public class StaticModel extends SingleKinematicModel {

  @Getter private final String name;
  @Getter private final SingleKinematicModel.Type type = SingleKinematicModel.Type.STATIC_MODEL;

  @Getter private final double x;
  @Getter private final double y;
  @Getter private final double theta;
  @Getter private final double vx = 0.0;
  @Getter private final double vy = 0.0;
  @Getter private final double ax = 0.0;
  @Getter private final double ay = 0.0;
  @Getter private final double alpha = 0.0;
  @Getter private final double omega = 0.0;

  @Getter private final Edge shape;
  @Getter private final Appearance appearance;

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

    Edge shape = Edge.of(map);
    Appearance appearance = Appearance.of(map);
    return helper.getBuilder().shape(shape).appearance(appearance).build();
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
    return ImmutableTable.of();
  }

  @Override
  public void paint() {
    Application.getPainterFactory().getEdgePainter().paint(x, y, theta, shape, appearance);
  }
}
