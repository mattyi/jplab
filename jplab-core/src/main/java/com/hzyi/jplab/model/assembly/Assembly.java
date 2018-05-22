package com.hzyi.jplab.model.assembly;

import com.hzyi.jplab.model.component.Component;
import com.hzyi.jplab.util.Buildable;
import java.util.HashMap;
import java.util.Map;

public class Assembly implements Buildable {

  private String name;
  private final Map<String, Component> components;

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

  public Builder newBuilder() {
    return new Builder();
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

    @Override
    public Builder mergeFrom(Builder builder) {
      throw new UnsupportedOperationException("Not implemented: mergeFrom()");
    }

    @Override
    public Assembly build() {
      
    }


  }

}