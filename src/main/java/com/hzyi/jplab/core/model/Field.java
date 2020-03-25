package com.hzyi.jplab.core.model;

import com.google.common.base.Preconditions;
import com.hzyi.jplab.core.model.kinematic.KinematicModel;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Field {

  private static final String CONST_FIELD = "CONST";
  private static final Field CONST = new Field("", CONST_FIELD);

  private final String model;
  private final String field;

  public Field(String model, String field) {
    this.model = Preconditions.checkNotNull(model);
    this.field = Preconditions.checkNotNull(field);
    if (model.isEmpty()) {
      Preconditions.checkArgument(
          field.equals(CONST_FIELD), "model is empty string, but field is not CONST");
    }
  }

  public Field(KinematicModel model, String field) {
    this(model.name(), field);
  }

  public static Field parse(String fullName) {
    if (fullName.equals("CONST")) {
      return CONST;
    }
    int index = fullName.indexOf('.');
    Preconditions.checkArgument(index != -1, "expecting full name, got: %s", fullName);
    String model = fullName.substring(0, index);
    String field = fullName.substring(index + 1);
    return new Field(model, field);
  }

  public String getFullName() {
    if (this == CONST) {
      return CONST_FIELD;
    }
    return String.format("%s.%s", model, field);
  }

  public String getModelName() {
    return model;
  }

  public String getShortName() {
    return field;
  }
}
