package com.hzyi.jplab.core.model;

import com.google.common.base.Preconditions;

import com.hzyi.jplab.core.util.Buildable;
import java.util.EnumMap;
import java.util.Map;

/** The dynamic state of a {@code Component} */
public class ComponentState {

  private final Component component;
  private final Map<Field, Double> fieldMap;

  public ComponentState(Component component) {
    this.component = component;
    this.fieldMap = new EnumMap<>();
  }

  public Component getComponent() {
    return this.component;
  }

  public Double get(Field field) {
    return fieldMap.get(field);
  }

  public Double put(Field field, double value) {
    return fieldMap.put(field, value);
  }
}