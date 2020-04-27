package com.hzyi.jplab.core.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Table;
import com.hzyi.jplab.core.model.kinematic.ConnectingModel;
import com.hzyi.jplab.core.model.kinematic.Field;
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
  static int i = 0;

  @Getter private final Map<String, KinematicModel> kinematicModels;
  @Getter private final Map<String, Field> fields;

  private AssemblySnapshot(Builder builder) {
    kinematicModels = ImmutableMap.copyOf(builder.kinematicModels);
    fields = ImmutableMap.copyOf(builder.fields);
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
            .map(KinematicModel::codependentPropertys)
            .flatMap(List::stream)
            .collect(ImmutableList.toImmutableList());

    DictionaryMatrix matrix = new DictionaryMatrix(keys);
    for (KinematicModel model : kinematicModels.values()) {
      for (Table.Cell<String, String, Double> cell :
          model.codependentMultipliers(timeStep).cellSet()) {
        String row = cell.getRowKey();
        String col = cell.getColumnKey();
        if (keys.contains(row) && (keys.contains(col) || col.equals(Property.constant()))) {
          matrix.add(row, col, cell.getValue());
        }
      }
    }

    for (Field field : fields.values()) {
      for (Table.Cell<String, String, Double> cell : field.codependentMultipliers().cellSet()) {
        String row = cell.getRowKey();
        String col = cell.getColumnKey();
        if (keys.contains(row) && (keys.contains(col) || col.equals(Property.constant()))) {
          matrix.add(row, col, cell.getValue());
        }
      }
    }
    return matrix;
  }

  public AssemblySnapshot merge(Map<String, Double> map) {
    Builder builder = toBuilder();
    for (Map.Entry<String, Double> entry : map.entrySet()) {
      Property f = Property.parse(entry.getKey());
      String model = f.getModel();
      String property = f.getProperty();
      builder.kinematicModel(
          model, builder.get(model).merge(ImmutableMap.of(property, entry.getValue())));
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
    builder.kinematicModels = new HashMap<>(kinematicModels);
    builder.fields = new HashMap<>(fields);
    return builder;
  }

  public static class Builder {

    private HashMap<String, KinematicModel> kinematicModels;
    private HashMap<String, Field> fields;

    public Builder() {
      kinematicModels = new HashMap<>();
      fields = new HashMap<>();
    }

    public Builder kinematicModel(String name, KinematicModel model) {
      kinematicModels.put(name, model);
      return this;
    }

    public Builder field(Field field) {
      fields.put(field.name(), field);
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
