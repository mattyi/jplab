package com.hzyi.jplab.core.model;

import java.util.Collection;
import java.util.Map;
import lombok.Builder;
import lombok.Singular;
import lombok.Getter;

@Builder(builderMethodName = "newBuilder")
public class Assembly implements Shape {

  @Singular private final Map<String, Component> components;
  @Getter private String name;

  @Getter private DisplayContext displayContext;
  
  public Collection<Component> getComponents() {
    return components.values();
  }

  public Component getComponent(String componentName) {
    return components.get(componentName);
  }

  public Painter<Shape> getPainter() {
    return new AssemblyPainter();
  }
}
