package com.hzyi.jplab.core.model;

import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Map;
import lombok.Getter;
import com.hzyi.jplab.core.viewer.shape.Shape;
import com.hzyi.jplab.core.viewer.PainterFactory;
import java.util.List;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Assembly {

  private final Map<String, Component> components = new HashMap<>();
  @Getter private final PainterFactory painterFactory;
  @Getter private final String name;
  
  public Assembly(String name, PainterFactory painterFactory) {
    this.name = name;
    this.painterFactory = painterFactory;
  }

  public Assembly withComponent(Component component) {
    Preconditions.checkArgument(!components.containsKey(component.getName()), "component with name %s already exists", component.getName());
    components.put(component.getName(), component);
    component.assembly(this);
    return this;
  }

  public Collection<Component> getComponents() {
    return components.values();
  }

  public Component getComponent(String componentName) {
    return components.get(componentName);
  }

  public void paint() {
    getPaintableComponents().stream().forEach(Shape::paint);
  }

  private List<Shape> getPaintableComponents() {
    return getComponents().stream().filter(c -> c instanceof Shape).map(c -> (Shape)c).collect(Collectors.toList());
  }
}
