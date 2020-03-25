package com.hzyi.jplab.core.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Table;
import com.hzyi.jplab.core.model.kinematic.ConnectingModel;
import com.hzyi.jplab.core.model.kinematic.KinematicModel;
import com.hzyi.jplab.core.util.DictionaryMatrix;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;

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

  public DictionaryMatrix toMatrix() {
    List<String> keys =
        kinematicModels.values().stream()
            .map(KinematicModel::codependentFields)
            .flatMap(List::stream)
            .collect(Collectors.toCollection(ArrayList::new));
    keys.add("CONST");
    DictionaryMatrix matrix = new DictionaryMatrix(keys);
    for (KinematicModel model : kinematicModels.values()) {
      for (Table.Cell<String, String, Double> cell : model.codependentMultipliers().cellSet()) {
        String row = cell.getRowKey();
        String col = cell.getColumnKey();
        if (keys.contains(row) && keys.contains(col)) {
          matrix.add(row, col, cell.getValue());
        }
      }
    }
    return matrix;
  }

  public AssemblySnapshot unpack(Map<String, Double> map) {
    Map<String, Map<String, Double>> modelMap = new HashMap<>();
    for (Map.Entry<String, Double> entry : map.entrySet()) {
      String fieldFullName = entry.getKey();
      String model = fieldFullName.substring(0, fieldFullName.indexOf('.'));
      String field = fieldFullName.substring(fieldFullName.indexOf('.') + 1);
      Map<String, Double> modelFields = modelMap.get(model);
      if (modelFields == null) {
        modelFields = new HashMap<>();
        modelMap.put(model, modelFields);
      }
      modelFields.put(field, entry.getValue());
    }
    return unpackImpl(modelMap);
  }

  private AssemblySnapshot unpackImpl(Map<String, Map<String, Double>> modelMap) {
    Builder builder = toBuilder();
    for (Map.Entry<String, Map<String, Double>> entry : modelMap.entrySet()) {
      KinematicModel model = builder.get(entry.getKey());
      model = model.unpack(entry.getValue());
      builder.kinematicModel(model.name(), model);
    }
    for (ConnectingModel connector : getConnectingModels()) {
      builder.kinematicModel(
          connector.name(),
          connector.unpack(
              ImmutableMap.of(
                  "connecting_model_a",
                  builder.get(connector.connectingModelA().name()),
                  "connecting_model_b",
                  builder.get(connector.connectingModelB().name()))));
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
