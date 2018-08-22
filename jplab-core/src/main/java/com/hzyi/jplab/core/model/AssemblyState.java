package com.hzyi.jplab.core.model;

import com.google.common.base.Preconditions;

import com.hzyi.jplab.core.model.ComponentState;
import com.hzyi.jplab.core.model.Field;
import com.hzyi.jplab.core.util.Buildable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AssemblyState implements Buildable {

  private final Map<String, ComponentState> componentStates;
  private final Assembly assembly;

  private AssemblyState(Builder builder) {
    this.componentStates = builder.componentStates;
    this.assembly = builder.assembly;
  }

  public Assembly getAssembly() {
    return this.assembly;
  }

  public ComponentState get(String componentName) {
    return Preconditions.checkNotNull(
        componentStates.get(componentName),
        "Component with name %s does not exist.",
        componentName);
  }

  public double get(String componentName, Field field) {
    ComponentState componentState = componentStates.get(componentName);
    if (componentState == null) {
      throw new IllegalArgumentException("Component with name " + componentName + " does not exist.");
    }
    return componentState.get(field);
  }

  public void set(String componentName, Field field, double value) {
    ComponentState componentState = componentStates.get(componentName);
    if (componentState == null) {
      throw new IllegalArgumentException("Component with name " + componentName + " does not exist.");
    }
    componentState.set(field, value);
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
    public Builder mergeFrom(Builder builder) {
      throw new UnsupportedOperationException("Not implemented: mergeFrom()");
    }

    @Override
    public AssemblyState build() {
      return new AssemblyState(this);
    }
  }


}