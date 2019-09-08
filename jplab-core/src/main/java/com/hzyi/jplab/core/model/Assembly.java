package com.hzyi.jplab.core.model;

import java.util.Collection;
import java.util.Map;
import lombok.Builder;
import lombok.Singular;
import lombok.Getter;
import com.hzyi.jplab.core.viewer.shape.Shape;
import com.hzyi.jplab.core.viewer.PainterFactory;
import java.util.List;
import java.util.stream.Collectors;

@Builder(builderMethodName = "newBuilder")
public class Assembly {

  @Singular private final Map<String, Component> components;
  @Getter private PainterFactory painterFactory;
  @Getter private String name;
  
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
