package com.hzyi.jplab.core.model;

import java.util.HashMap;
import java.util.Map;

/** The dynamic state of a {@code Component} */
public class ComponentState {

  private final Component component;
  private final Map<Field, Double> fieldMap;

  public ComponentState(Component component) {
    this.component = component;
    this.fieldMap = new HashMap<>();
  }

  public Component getComponent() {
    return this.component;
  }

  public Double get(Field field) {
    return fieldMap.get(field);
  }

  public ComponentState put(Field field, double value) {
    fieldMap.put(field, value);
    return this;
  }
}