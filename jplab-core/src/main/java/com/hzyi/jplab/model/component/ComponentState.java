package com.hzyi.jplab.model.component;

import com.hzyi.jplab.util.Buildable;
import java.util.HashMap;
import java.util.Map;

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

  public static Builder newBuilder(Component component) {
    return new Builder(component);
  }

  public static final class Builder implements 
      com.hzyi.jplab.util.Builder<Builder> {

    private final Component component;
    private final Map<Field, Double> fields = new HashMap();

    public Builder(Component component) {
      this.component = component;
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
    public Builder mergeFrom(com.hzyi.jplab.util.Builder<Builder> builder) {
      throw new UnsupportedOperationException("Not implemented: mergeFrom()");
    }

    @Override
    public ComponentState build() {
      return new ComponentState(this);
    }
 
  }


}