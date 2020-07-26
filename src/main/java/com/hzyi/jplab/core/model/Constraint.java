package com.hzyi.jplab.core.model;

import com.google.common.base.Preconditions;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public class Constraint {

  @Getter private final String model;
  @Getter private final String constraint;

  public Constraint(String model, String constraint) {
    this.model = Preconditions.checkNotNull(model, "internal: model can't be null");
    this.constraint = Preconditions.checkNotNull(constraint, "internal: constraint can't be null");
    Preconditions.checkArgument(!this.model.isEmpty(), "internal: model can't be an empty string.");
    Preconditions.checkArgument(
        !this.constraint.isEmpty(), "internal: constraint can't be an empty string");
  }

  public Constraint(Component component, String constraint) {
    this(component.name(), constraint);
  }

  public static Constraint cof(Component component, String constraint) {
    return new Constraint(component, constraint);
  }

  public static Constraint cof(String fullName) {
    return parse(fullName);
  }

  public static Constraint parse(String fullName) {
    int index = fullName.indexOf('.');
    Preconditions.checkArgument(index != -1, "expecting full name, got: %s", fullName);
    String model = fullName.substring(0, index);
    String constraint = fullName.substring(index + 1);
    return new Constraint(model, constraint);
  }

  public static String format(Component component, String constraint) {
    return new Constraint(component, constraint).toString();
  }

  public String toString() {
    return String.format("%s.%s", model, constraint);
  }
}
