package com.hzyi.jplab.core.model;

import com.google.common.base.Preconditions;
import com.hzyi.jplab.core.model.kinematic.KinematicModel;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Property {

  private static final String CONST_FIELD = "_constant";
  private static final Property CONST = new Property("", CONST_FIELD);

  private final String model;
  private final String property;

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
    return new Property(model, property).getFullName();
  }

  public static String constant() {
    return CONST_FIELD;
  }

  public String getFullName() {
    if (this == CONST) {
      return CONST_FIELD;
    }
    return String.format("%s.%s", model, property);
  }

  public String getModel() {
    return model;
  }

  public String getProperty() {
    return property;
  }
}
