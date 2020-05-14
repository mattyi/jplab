package com.hzyi.jplab.core.model;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.collect.ImmutableList;
import com.hzyi.jplab.core.application.Application;
import com.hzyi.jplab.core.model.kinematic.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.ToString;

@ToString
public class Assembly {

  private static final Assembly INSTANCE = new Assembly();

  private final Map<String, Component> components = new HashMap<>();
  private final Map<String, Field> fields = new HashMap<>();
  private final AssemblySnapshot initialAssemblySnapshot = AssemblySnapshot.empty();
  private boolean isImmutable = false;

  private Assembly() {}

  public static Assembly getInstance() {
    return INSTANCE;
  }

  /**
   * Add a component to the assembly. The name must be unique and the assembly must not be
   * immutable.
   */
  public Assembly withComponent(Component component) {
    Component old = components.put(component.getName(), component);
    checkState(!isImmutable, "assembly is immutable");
    checkArgument(old == null, "component with name %s already exists", component.getName());
    initialAssemblySnapshot.withKinematicModel(component.getInitialKinematicModel());
    return this;
  }

  /**
   * Adds a field to the assembly. The name must be unique and the assembly must not be immutable.
   */
  public Assembly withField(Field field) {
    Field old = fields.put(field.name(), field);
    checkState(!isImmutable, "assembly is immutable");
    checkArgument(old == null, "component with name %s already exists", field.name());
    return this;
  }

  /**
   * Make the assembly immutable. After this method is called, all changes made to the assembly will
   * be illegal.
   */
  public void makeImmutable() {
    isImmutable = true;
    initialAssemblySnapshot.makeImmutable();
  }

  /** Returns an immutable copy of the components in the assembly. */
  public List<Component> getComponents() {
    return ImmutableList.copyOf(components.values());
  }

  /** Returns a component by its name, or null if there is no such component. */
  public Component getComponent(String componentName) {
    return components.get(componentName);
  }

  /** Returns an immutable copy of the fields in the assembly. */
  public List<Field> getFields() {
    return ImmutableList.copyOf(fields.values());
  }

  /** Returns a field by its name, or null if there is no such field. */
  public Field getField(String fieldName) {
    return fields.get(fieldName);
  }

  public AssemblySnapshot getInitialAssemblySnapshot() {
    return initialAssemblySnapshot;
  }

  public void paint(AssemblySnapshot assemblySnapshot) {
    Application.getPainterFactory().clearCanvas();
    components.values().stream().forEach(c -> c.paint(c.getKinematicModel(assemblySnapshot)));
  }

  public void paint() {
    paint(getInitialAssemblySnapshot());
  }
}
