package com.hzyi.jplab.core.model;

import java.util.Collection;
import java.util.Map;
import com.hzyi.jplab.view.Appearance;
import lombok.Builder;
import lombok.Singular;
import lombok.Getter;

@Builder(builderMethodName = "newBuilder")
public class Assembly {

  @Singular private final Map<String, Component> components;
  @Getter private String name;
  @Getter private Appearance appearance;
  
  public Collection<Component> getComponents() {
    return components.values();
  }

  public Component getComponent(String componentName) {
    return components.get(componentName);
  }

  public void paint() {
    getComponents().stream().forEach();
  }
}
