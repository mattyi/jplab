package com.hzyi.jplab.core.model;

import com.google.common.base.Preconditions;

import com.hzyi.jplab.core.util.Buildable;
import java.util.HashMap;
import java.util.Map;

/** The dynamic state of a {@code Component} */
public class ComponentState implements Buildable {

  private final Component component;
  private final Map<Field, Double> fields;

  public Component getComponent() {
    return this.component;
  }

  public double get(Field field) {
    return fields.get(field);
  }

  public void set(Field field, double value) {
    if (!fields.containsKey(field)) {
      throw new IllegalArgumentException("Field " + field + " does not exist.");
    }
    fields.put(field, value);
  }

  private ComponentState(Builder builder) {
    this.component = builder.component;
    this.fields = builder.fields;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static final class Builder implements 
      com.hzyi.jplab.core.util.Builder<Builder> {

    private Component component;
    private final Map<Field, Double> fields = new HashMap();

    public Builder setComponent(Component component) {
      this.component = component;
      return this;
    }

    /**
     * This method sets the {@code Component.FIELD} to {@code value}.
     * If the {@code Component.FIELD} was set before, this method 
     * overrides it.
     */
    public Builder set(Field field, double value) {
      fields.put(field, value);
      return this;
    }

    @Override
    public ComponentState build() {
      Preconditions.checkNotNull(component, "component can't be null.");
      return new ComponentState(this);
    }
 
  }


}