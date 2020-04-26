package com.hzyi.jplab.core.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Table;
import com.hzyi.jplab.core.model.kinematic.ConnectingModel;
import com.hzyi.jplab.core.model.kinematic.KinematicModel;
import com.hzyi.jplab.core.util.DictionaryMatrix;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.ToString;

@ToString
public class AssemblySnapshot {

  @Getter private final Map<String, KinematicModel> kinematicModels;

  private AssemblySnapshot(Builder builder) {
    kinematicModels = ImmutableMap.copyOf(builder.kinematicModels);
  }

  public KinematicModel get(String name) {
    return kinematicModels.get(name);
  }

  public List<ConnectingModel> getConnectingModels() {
    return kinematicModels.values().stream()
        .filter(m -> m instanceof ConnectingModel)
        .map(m -> (ConnectingModel) m)
        .collect(Collectors.toList());
  }

  public DictionaryMatrix getCodependentMatrix(double timeStep) {
    List<String> keys =
        kinematicModels.values().stream()
            .map(KinematicModel::codependentFields)
            .flatMap(List::stream)
            .collect(ImmutableList.toImmutableList());

    DictionaryMatrix matrix = new DictionaryMatrix(keys);
    for (KinematicModel model : kinematicModels.values()) {
      for (Table.Cell<String, String, Double> cell :
          model.codependentMultipliers(timeStep).cellSet()) {
        String row = cell.getRowKey();
        String col = cell.getColumnKey();
        if (keys.contains(row) && (keys.contains(col) || col.equals(Field.constant()))) {
          matrix.add(row, col, cell.getValue());
        }
      }
    }
    return matrix;
  }

  public AssemblySnapshot merge(Map<String, Double> map) {
    Builder builder = toBuilder();
    for (Map.Entry<String, Double> entry : map.entrySet()) {
      Field f = Field.parse(entry.getKey());
      String model = f.getModel();
      String field = f.getField();
      builder.kinematicModel(
          model, builder.get(model).merge(ImmutableMap.of(field, entry.getValue())));
    }

    for (ConnectingModel connector : getConnectingModels()) {
      KinematicModel modelA = builder.get(connector.connectingModelA().name());
      KinematicModel modelB = builder.get(connector.connectingModelB().name());
      builder.kinematicModel(
          connector.name(),
          connector.merge(
              ImmutableMap.of("connecting_model_a", modelA, "connecting_model_b", modelB)));
    }
    return builder.build();
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public Builder toBuilder() {
    Builder builder = new Builder();
    for (Map.Entry<String, KinematicModel> entry : kinematicModels.entrySet()) {
      builder.kinematicModel(entry.getKey(), entry.getValue());
    }
    return builder;
  }

  public static class Builder {

    private HashMap<String, KinematicModel> kinematicModels;

    public Builder() {
      kinematicModels = new HashMap<>();
    }

    public Builder kinematicModel(String name, KinematicModel model) {
      kinematicModels.put(name, model);
      return this;
    }

    public Builder set(String name, KinematicModel model) {
      return kinematicModel(name, model);
    }

    public KinematicModel get(String name) {
      return kinematicModels.get(name);
    }

    public AssemblySnapshot build() {
      return new AssemblySnapshot(this);
    }
  }
}
