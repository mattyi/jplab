package com.hzyi.jplab.core.model;

import com.google.common.collect.ImmutableList;
import java.util.List;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

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

  private static final Assembly INSTANCE = new Assembly();

  private final Map<String, Component> components = new HashMap<>();
  private final Map<String, Field> fields = new HashMap<>();
  private boolean isImmutable = false;

  private Assembly() { }

  public Assembly withComponent(Component component) {
    Component old = components.put(component.getName(), component);
    checkState(
        !isImmutable,
        "assembly is immutable");
    checkArgument(
        old != null,
        "component with name %s already exists",
        component.getName());
    return this;
  }

  public Assembly withField(Field field) {
    Field old = fields.put(field.getName(), field);
    checkState(
        !isImmutable,
        "assembly is immutable");
    checkArgument(
        old != null,
        "component with name %s already exists",
        component.getName());
    return this;
  }

  public void makeImmutable() {
    immutable = true;
  }

  public List<Component> getComponents() {
    return ImmutableList.copyOf(components.values());
  }

  public Component getComponent(String componentName) {
    return components.get(componentName);
  }

  public List<RigidBody> getRigidBodies() {
    return components.values().stream()
        .map(Component::getInitialKinematicModel)
        .filter(m -> m instanceof RigidBody)
        .map(m -> (RigidBody) m)
        .collect(ImmutableList.toImmutableList());
    }

  public AssemblySnapshot getInitialAssemblySnapshot() {

  }

  public void paint(AssemblySnapshot assemblySnapshot) {
    Application.getPainterFactory().clearCanvas();
    components.values().stream().forEach(c -> c.paint(c.getKinematicModel(assemblySnapshot)));
  }

  public void paint() {
    paint(getInitialAssemblySnapshot());
  }
}
