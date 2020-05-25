package com.hzyi.jplab.core.model;

import com.google.common.base.Preconditions;
import com.hzyi.jplab.core.model.kinematic.KinematicModel;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public class Property {

  private static final String CONST_FIELD = "_constant";
  private static final Property CONST = new Property("", CONST_FIELD);

  @Getter private final String model;
  @Getter private final String property;

  public Property(String model, String property) {
    this.model = Preconditions.checkNotNull(model);
    this.property = Preconditions.checkNotNull(property);
    if (model.isEmpty()) {
      Preconditions.checkArgument(
          property.equals(CONST_FIELD), "model is empty string, but property is not CONST");
    }
  }

  public Property(KinematicModel model, String property) {
    this(model.name(), property);
  }

  public static Property pof(KinematicModel model, String property) {
    return new Property(model, property);
  }

  public static Property pof(String fullName) {
    return parse(fullName);
  }

  public static Property parse(String fullName) {
    if (fullName.equals(CONST_FIELD)) {
      return CONST;
    }
    int index = fullName.indexOf('.');
    Preconditions.checkArgument(index != -1, "expecting full name, got: %s", fullName);
    String model = fullName.substring(0, index);
    String property = fullName.substring(index + 1);
    return new Property(model, property);
  }

  public static String format(KinematicModel model, String property) {
    return new Property(model, property).toString();
  }

  public static Property constant() {
    return CONST;
  }

  public String toString() {
    if (this == CONST) {
      return CONST_FIELD;
    }
    return String.format("%s.%s", model, property);
  }
}
