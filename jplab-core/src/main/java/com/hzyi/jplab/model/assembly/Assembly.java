package com.hzyi.jplab.model.assembly;

import com.hzyi.jplab.model.component.Component;
import com.hzyi.jplab.util.Buildable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Assembly implements Buildable {

  private String name;
  private final Map<String, Component> components;
  private final AssemblyState initState;

  private Assembly(Builder builder) {
    this.name = builder.name;
    this.components = builder.components;
    this.initState = newAssemblyStateBuilder(builder).build();
  }

  public String getName() {
    return name;
  }

  public Component getComponent(String name) {
    Component component = components.get(name);
    if (component == null) {
      throw new IllegalArgumentException("Component with name " + name + " does not exist.");
    }
    return component;
  }

  public AssemblyState getInitialAssemblyState() {
    return this.initState;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  private AssemblyState.Builder newAssemblyStateBuilder(Builder builder) {
    return AssemblyState
        .newBuilder()
        .setAssembly(this)
        .addAll(builder.components.values().stream().map(x -> x.getInitialComponentState()).collect(Collectors.toList()));
  }

  public static class Builder implements com.hzyi.jplab.util.Builder<Builder> {
    
    private String name;
    private final Map<String, Component> components = new HashMap<>();

    public Builder setName(String name) {
      this.name = name;
      return this;
    }

    public Builder add(Component component) {
      if (components.containsKey(component.getName())) {
        throw new IllegalArgumentException("Component name already exists.");
      }
      this.components.put(component.getName(), component);
      return this;
    }

    public Builder addAll(Collection<Component> components) {
      for (Component component : components) {
        add(component);
      }
      return this;
    }

    @Override
    public Builder mergeFrom(Builder builder) {
      throw new UnsupportedOperationException("Not implemented: mergeFrom()");
    }

    @Override
    public Assembly build() {
      return new Assembly(this);
    }


  }

}