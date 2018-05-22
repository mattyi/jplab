package com.hzyi.jplab.model.assembly;

import com.hzyi.jplab.model.component.ComponentState;
import com.hzyi.jplab.model.component.Field;
import com.hzyi.jplab.util.Buildable;
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

  public static class Builder implements com.hzyi.jplab.util.Builder<Builder> {
    
    private Map<String, ComponentState> componentStates = new HashMap<>();
    private Assembly assembly;

    /**
     * componentStates with the same name will override the previous one.
     */
    public Builder add(ComponentState componentState) {
      componentStates.put(componentState.getComponent().getName(), componentState);
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