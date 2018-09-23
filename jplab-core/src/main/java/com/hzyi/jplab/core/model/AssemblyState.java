package com.hzyi.jplab.core.model;

import com.google.common.base.Preconditions;

import com.hzyi.jplab.core.model.ComponentState;
import com.hzyi.jplab.core.model.Field;
import com.hzyi.jplab.core.util.Buildable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/** The dynamic state of an {@code Assembly}. */
public class AssemblyState implements Buildable {

  private final Map<String, ComponentState> componentStates;
  private final Assembly assembly;

  private AssemblyState(Builder builder) {
    this.componentStates = builder.componentStates;
    this.assembly = builder.assembly;
  }

  /**
   * @returns the {@code Assembly} this {@code AssemblyState} is describing.
   *
   */
  public Assembly getAssembly() {
    return this.assembly;
  }

  /**
   * @returns the {@code ComponentState} based on the name of a {@code component}.
   * @throws NullPointerException if the {@code Component} does not exist.
   *
   */
  public ComponentState get(String componentName) {
    return Preconditions.checkNotNull(
        componentStates.get(componentName),
        "Component with name %s does not exist.",
        componentName);
  }

  /**
   * @returns the state value based on the name of a {@code Component} and the {@code Field}.
   * @throws NullPointerException if the {@code Component} or the {@code Field} does not exist.
   *
   */
  public double get(String componentName, Field field) {
    ComponentState componentState = componentStates.get(componentName);
    if (componentState == null) {
      throw new IllegalArgumentException("Component with name " + componentName + " does not exist.");
    }
    return componentState.get(field);
  }

  /**
   * Updates the value of a {@code Field} of a {@code Component}.
   *
   */
  public void set(String componentName, Field field, double value) {
    ComponentState componentState = componentStates.get(componentName);
    if (componentState == null) {
      throw new IllegalArgumentException("Component with name " + componentName + " does not exist.");
    }
    componentState.set(field, value);
  }

  /**
   * @returns a collection of {@code ComponentState}.
   *
   */
  public Iterable<ComponentState> getComponentStates() {
    return componentStates.values();
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder implements com.hzyi.jplab.core.util.Builder<Builder> {
    
    private Map<String, ComponentState> componentStates = new HashMap<>();
    private Assembly assembly;

    /**
     * componentStates with the same name will override the previous one.
     */
    public Builder add(ComponentState componentState) {
      componentStates.put(componentState.getComponent().getName(), componentState);
      return this;
    }

    public Builder addAll(Collection<ComponentState> componentStates) {
      for (ComponentState state : componentStates) {
        add(state);
      }
      return this;
    }

    public Builder setAssembly(Assembly assembly) {
      this.assembly = assembly;
      return this;
    }

    @Override
    public AssemblyState build() {
      return new AssemblyState(this);
    }
  }


}