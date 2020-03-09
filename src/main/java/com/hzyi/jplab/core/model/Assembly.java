package com.hzyi.jplab.core.model;

import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

public class Assembly {

  private final Map<String, Component> components = new HashMap<>();
  @Getter private final String name;

  public Assembly(String name) {
    this.name = name;
  }

  public Assembly withComponent(Component component) {
    Preconditions.checkArgument(
        !components.containsKey(component.getName()),
        "component with name %s already exists",
        component.getName());
    components.put(component.getName(), component);
    return this;
  }

  public Collection<Component> getComponents() {
    return components.values();
  }

  public Component getComponent(String componentName) {
    return components.get(componentName);
  }

  public AssemblySnapshot getInitialAssemblySnapshot() {
    AssemblySnapshot.Builder initialAssemblySnapshot = AssemblySnapshot.newBuilder();
    for (Component component : getComponents()) {
      initialAssemblySnapshot.set(component.getName(), component.getInitialKinematicModel());
    }
    return initialAssemblySnapshot.build();
  }

  public void paint(AssemblySnapshot assemblySnapshot) {
    components.values().stream().forEach(c -> c.paint(c.getKinematicModel(assemblySnapshot)));
  }

  public void paint() {
    paint(getInitialAssemblySnapshot());
  }
}
