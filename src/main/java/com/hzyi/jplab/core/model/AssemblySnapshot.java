package com.hzyi.jplab.core.model;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Streams;
import com.google.common.collect.Table;
import com.hzyi.jplab.core.application.Application;
import com.hzyi.jplab.core.model.kinematic.Connector;
import com.hzyi.jplab.core.model.kinematic.KinematicModel;
import com.hzyi.jplab.core.model.kinematic.MultiplierProvider;
import com.hzyi.jplab.core.model.kinematic.RigidBody;
import com.hzyi.jplab.core.model.kinematic.VerifierProvider;
import com.hzyi.jplab.core.timeline.Verifier;
import com.hzyi.jplab.core.util.DictionaryMatrix;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.ToString;

@ToString
public class AssemblySnapshot {

  private final Map<String, KinematicModel> kinematicModels;
  private boolean isImmutable;

  private AssemblySnapshot() {
    this.kinematicModels = new HashMap<>();
  }

  /** Returns an empty snapshot. */
  public static AssemblySnapshot empty() {
    return new AssemblySnapshot();
  }

  /** Returns a mutable copy of this snapshot. */
  public AssemblySnapshot copy() {
    AssemblySnapshot copy = empty();
    for (KinematicModel model : kinematicModels.values()) {
      copy.withKinematicModel(model);
    }
    return copy;
  }

  /** Adds the model to this snapshot. Error if there are two models with the same name. */
  public AssemblySnapshot withKinematicModel(KinematicModel model) {
    KinematicModel old = this.kinematicModels.put(model.name(), model);
    checkState(!isImmutable, "snapshot is immutable");
    checkArgument(old == null, "component with name %s already exists", model.name());
    return this;
  }

  /** Adds the model to this snapshot. Replace the old one if two models have the same name. */
  public AssemblySnapshot updateKinematicModel(KinematicModel model) {
    KinematicModel old = kinematicModels.put(model.name(), model);
    checkState(!isImmutable, "snapshot is immutable");
    return this;
  }

  /** Returns a kinematic model by its name, or null if there is no such model. */
  public KinematicModel getKinematicModel(String name) {
    return kinematicModels.get(name);
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
    return kinematicModels.values().stream()
        .filter(KinematicModel::isConnector)
        .map(m -> (Connector) m)
        .collect(Collectors.toList());
  }

  /** Returns an immutable copy of all the rigid body models in the assembly. */
  public List<RigidBody> getRigidBodies() {
    return kinematicModels.values().stream()
        .filter(KinematicModel::isRigidBody)
        .map(m -> (RigidBody) m)
        .collect(ImmutableList.toImmutableList());
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
    return kinematicModels.values().stream()
        .filter(p -> p instanceof VerifierProvider)
        .map(p -> (VerifierProvider) p)
        .flatMap(p -> p.verifiers().stream())
        .collect(ImmutableList.toImmutableList());
  }

  public AssemblySnapshot merge(Table<String, String, Double> table) {
    AssemblySnapshot copy = copy();
    for (Map.Entry<String, Map<String, Double>> entry : table.rowMap().entrySet()) {
      String model = entry.getKey();
      Map<String, Double> data = entry.getValue();
      copy.updateKinematicModel(getKinematicModel(model).merge(data));
    }
    copy.updateConnectors();
    copy.makeImmutable();
    return copy;
  }

  private List<Constraint> constraints() {
    return kinematicModels.values().stream()
        .map(KinematicModel::constraints)
        .flatMap(List::stream)
        .collect(ImmutableList.toImmutableList());
  }

  private List<Property> properties() {
    return Streams.concat(
            kinematicModels.values().stream().map(KinematicModel::properties).flatMap(List::stream),
            Stream.of(Property.constant()))
        .collect(ImmutableList.toImmutableList());
  }

  private List<MultiplierProvider> multiplierProviders() {
    return Streams.concat(
            kinematicModels.values().stream().map(m -> (MultiplierProvider) m),
            Application.getAssembly().getFields().stream().map(f -> (MultiplierProvider) f))
        .collect(ImmutableList.toImmutableList());
  }

  private void updateConnectors() {
    for (Connector connector : getConnectors()) {
      KinematicModel modelU = getKinematicModel(connector.modelU().name());
      KinematicModel modelV = getKinematicModel(connector.modelV().name());
      KinematicModel updatedConnector =
          connector.merge(ImmutableMap.of("model_u", modelU, "model_v", modelV));
      updateKinematicModel(updatedConnector);
    }
  }
}
