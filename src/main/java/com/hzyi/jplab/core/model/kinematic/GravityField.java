package com.hzyi.jplab.core.model.kinematic;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.hzyi.jplab.core.application.Application;
import com.hzyi.jplab.core.model.Assembly;
import com.hzyi.jplab.core.model.Property;
import com.hzyi.jplab.core.util.UnpackHelper;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Builder(builderMethodName = "newBuilder", toBuilder = true)
@ToString
public class GravityField implements Field {

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

  public Table<String, String, Double> codependentMultipliers() {
    Assembly assembly = Application.getAssembly();
    ImmutableTable.Builder<String, String, Double> builder = ImmutableTable.builder();
    for (RigidBody rigidBody : assembly.getInitialAssemblySnapshot().getRigidBodies()) {
      builder.put(Property.format(rigidBody, "ax"), Property.constant(), gx * rigidBody.mass());
      builder.put(Property.format(rigidBody, "ay"), Property.constant(), gy * rigidBody.mass());
      // TODO: support rotation
    }
    return builder.build();
  }
}
