package com.hzyi.jplab.core.model;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Table;
import com.hzyi.jplab.core.model.kinematic.Connector;
import com.hzyi.jplab.core.model.kinematic.ConstraintProvider;
import com.hzyi.jplab.core.model.kinematic.MultiplierProvider;
import com.hzyi.jplab.core.model.kinematic.PropertyProvider;
import com.hzyi.jplab.core.model.kinematic.RigidBody;
import com.hzyi.jplab.core.model.kinematic.SingleKinematicModel;
import com.hzyi.jplab.core.model.kinematic.VerifierProvider;
import com.hzyi.jplab.core.model.shape.Paintable;
import com.hzyi.jplab.core.timeline.Verifier;
import com.hzyi.jplab.core.util.DictionaryMatrix;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.ToString;

@ToString
public class Assembly {

  private final Map<String, Component> components;
  private boolean isImmutable;

  private Assembly() {
    this.components = new HashMap<>();
  }

  /** Returns an empty assembly. */
  public static Assembly empty() {
    return new Assembly();
  }

  /** Returns a mutable copy of this assembly. */
  public Assembly copy() {
    Assembly copy = empty();
    for (Component component : components.values()) {
      copy.withComponent(component);
    }
    return copy;
  }

  /** Adds the model to this snapshot. Error if there are two models with the same name. */
  public Assembly withComponent(Component component) {
    checkState(!isImmutable, "assebmly is immutable");
    Component old = this.components.put(component.name(), component);
    checkArgument(old == null, "component with name %s already exists", component.name());
    return this;
  }

  /** Adds the model to this snapshot. Replace the old one if two models have the same name. */
  public Assembly updateComponent(Component component) {
    checkState(!isImmutable, "snapshot is immutable");
    Component old = components.put(component.name(), component);
    return this;
  }

  /** Returns a kinematic model by its name, or null if there is no such model. */
  public Component getComponent(String name) {
    return components.get(name);
  }

  public SingleKinematicModel getKinematicModel(String name) {
    Component component = getComponent(name);
    if (!(component instanceof SingleKinematicModel)) {
      return null;
    }
    return (SingleKinematicModel) component;
  }

  public Connector getConnector(String name) {
    Component component = getComponent(name);
    if (!(component instanceof Connector)) {
      return null;
    }
    return (Connector) component;
  }

  public PropertyProvider getPropertyProvider(String name) {
    Component component = getComponent(name);
    if (!(component instanceof PropertyProvider)) {
      return null;
    }
    return (PropertyProvider) component;
  }

  /**
   * Make the assembly immutable. After this method is called, all attempts to make change to this
   * assembly will be illegal.
   */
  public void makeImmutable() {
    this.isImmutable = true;
  }

  /** Returns an immutable copy of all the connectors in the assembly. */
  public List<Connector> getConnectors() {
    return components.values().stream()
        .filter(Components::isConnector)
        .map(m -> (Connector) m)
        .collect(Collectors.toList());
  }

  /** Returns an immutable copy of all the rigid body models in the assembly. */
  public List<RigidBody> getRigidBodies() {
    return components.values().stream()
        .filter(m -> m instanceof RigidBody)
        .map(m -> (RigidBody) m)
        .collect(ImmutableList.toImmutableList());
  }

  public void paint() {
    components.values().stream()
        .filter(Components::isPaintable)
        .map(p -> (Paintable) p)
        .forEach(Paintable::paint);
  }

  public DictionaryMatrix getCodependentMatrix(double timeStep) {
    List<Constraint> constraints = constraints();
    List<Property> properties = properties();
    DictionaryMatrix matrix = new DictionaryMatrix(constraints(), properties());
    for (MultiplierProvider mp : multiplierProviders()) {
      for (Table.Cell<Constraint, Property, Double> cell :
          mp.codependentMultipliers(timeStep).cellSet()) {
        Constraint constraint = cell.getRowKey();
        Property property = cell.getColumnKey();
        if (constraints.contains(constraint) && (properties.contains(property))) {
          matrix.add(constraint, property, cell.getValue());
        }
      }
    }
    return matrix;
  }

  public List<Verifier> getVerifiers() {
    return components.values().stream()
        .filter(p -> p instanceof VerifierProvider)
        .map(p -> (VerifierProvider) p)
        .flatMap(p -> p.verifiers().stream())
        .collect(ImmutableList.toImmutableList());
  }

  public Assembly merge(Table<String, String, Double> table) {
    Assembly copy = copy();
    for (Map.Entry<String, Map<String, Double>> entry : table.rowMap().entrySet()) {
      String model = entry.getKey();
      Map<String, Double> data = entry.getValue();
      copy.updateComponent((getPropertyProvider(model)).merge(data));
    }
    copy.updateConnectors();
    copy.makeImmutable();
    return copy;
  }

  private List<Constraint> constraints() {
    return components.values().stream()
        .filter(Components::hasConstraints)
        .map(m -> (ConstraintProvider) m)
        .flatMap(cp -> cp.constraints().stream())
        .collect(ImmutableList.toImmutableList());
  }

  private List<Property> properties() {
    List<Property> answer =
        components.values().stream()
            .filter(Components::hasProperties)
            .map(m -> (PropertyProvider) m)
            .flatMap(p -> p.properties().stream())
            .collect(ImmutableList.toImmutableList());
    return ImmutableList.<Property>builder().addAll(answer).add(Property.constant()).build();
  }

  private List<MultiplierProvider> multiplierProviders() {
    return components.values().stream()
        .filter(Components::hasMultipliers)
        .map(m -> (MultiplierProvider) m)
        .collect(ImmutableList.toImmutableList());
  }

  private void updateConnectors() {
    for (Connector connector : getConnectors()) {
      SingleKinematicModel modelU = (SingleKinematicModel) getComponent(connector.modelU().name());
      SingleKinematicModel modelV = (SingleKinematicModel) getComponent(connector.modelV().name());
      Component updatedConnector =
          connector.merge(ImmutableMap.of("model_u", modelU, "model_v", modelV));
      updateComponent(updatedConnector);
    }
  }
}
