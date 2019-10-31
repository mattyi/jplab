package com.hzyi.jplab.core.model;

import com.google.common.base.Preconditions;
import com.hzyi.jplab.core.painter.PainterFactory;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

public class Assembly {

  private final Map<String, Component> components = new HashMap<>();
  @Getter private final PainterFactory painterFactory;
  @Getter private final String name;

  public Assembly(String name, PainterFactory painterFactory) {
    this.name = name;
    this.painterFactory = painterFactory;
  }

  public Assembly withComponent(Component component) {
    Preconditions.checkArgument(
        !components.containsKey(component.getName()),
        "component with name %s already exists",
        component.getName());
    components.put(component.getName(), component);
    component.setAssembly(this);
    return this;
  }

  public Collection<Component> getComponents() {
    return components.values();
  }

  public Component getComponent(String componentName) {
    return components.get(componentName);
  }

  public void paint(AssemblySnapshot assemblySnapshot) {
    components.valueSet().stream().forEach(c -> c.paint(c.getKinematicModel(assemblySnapshot)));
  }
}
