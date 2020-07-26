package com.hzyi.jplab.core.model.kinematic;

import static com.hzyi.jplab.core.model.Constraint.cof;
import static com.hzyi.jplab.core.model.Property.constant;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.hzyi.jplab.core.application.Application;
import com.hzyi.jplab.core.model.Assembly;
import com.hzyi.jplab.core.model.Constraint;
import com.hzyi.jplab.core.model.Property;
import com.hzyi.jplab.core.util.UnpackHelper;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

/** A GravityField is gravity field. It applies to all RigidBody components. */
@Accessors(fluent = true)
@Builder(builderMethodName = "newBuilder", toBuilder = true)
@ToString
public class GravityField implements Field, MultiplierProvider {

  private static final String NAME = "gravity_field";

  @Getter @Builder.Default private final double gx = 0;
  @Getter @Builder.Default private final double gy = -9.8;

  public String name() {
    return NAME;
  }

  public static GravityField of(Map<String, Object> map) {
    GravityFieldBuilder builder = GravityField.newBuilder();
    UnpackHelper<GravityFieldBuilder> helper = UnpackHelper.of(builder, map, GravityField.class);
    helper.unpack("gx", Double.class, GravityFieldBuilder::gx);
    helper.unpack("gy", Double.class, GravityFieldBuilder::gy);
    return helper.getBuilder().build();
  }

  @Override
  public Table<Constraint, Property, Double> codependentMultipliers(double timeStep) {
    Assembly assembly = Application.getInitialAssembly();
    ImmutableTable.Builder<Constraint, Property, Double> builder = ImmutableTable.builder();
    for (RigidBody rigidBody : assembly.getRigidBodies()) {
      builder.put(cof(rigidBody, "ax-upwind-balance"), constant(), gx * rigidBody.mass());
      builder.put(cof(rigidBody, "ay-upwind-balance"), Property.constant(), gy * rigidBody.mass());
      // TODO: support rotation
    }
    return builder.build();
  }
}
