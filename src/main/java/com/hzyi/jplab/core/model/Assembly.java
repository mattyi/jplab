package com.hzyi.jplab.core.model;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.hzyi.jplab.core.application.Application;
import com.hzyi.jplab.core.model.kinematic.Field;
import com.hzyi.jplab.core.model.kinematic.RigidBody;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.ToString;

@ToString
public class Assembly {

  private final Map<String, Component> components = new HashMap<>();
  private final Map<String, Field> fields = new HashMap<>();
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

  public Assembly withField(Field field) {
    Preconditions.checkArgument(
        !fields.containsKey(field.name()), "field with name %s already exists", field.name());
    fields.put(field.name(), field);
    return this;
  }

  public Collection<Component> getComponents() {
    return components.values();
  }

  public Component getComponent(String componentName) {
    return components.get(componentName);
  }

  public List<RigidBody> getRigidBodies() {
    List<RigidBody> answer =
        components.values().stream()
            .map(Component::getInitialKinematicModel)
            .filter(m -> m instanceof RigidBody)
            .map(m -> (RigidBody) m)
            .collect(ImmutableList.toImmutableList());
    return answer;
  }

  public AssemblySnapshot getInitialAssemblySnapshot() {
    AssemblySnapshot.Builder initialAssemblySnapshot = AssemblySnapshot.newBuilder();
    for (Component component : getComponents()) {
      initialAssemblySnapshot.set(component.getName(), component.getInitialKinematicModel());
    }
    for (Field field : fields.values()) {
      initialAssemblySnapshot.field(field);
    }
    return initialAssemblySnapshot.build();
  }

  public void paint(AssemblySnapshot assemblySnapshot) {
    Application.getPainterFactory().clearCanvas();
    components.values().stream().forEach(c -> c.paint(c.getKinematicModel(assemblySnapshot)));
  }

  public void paint() {
    paint(getInitialAssemblySnapshot());
  }
}
