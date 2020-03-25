package com.hzyi.jplab.core.model.kinematic;

import com.google.common.collect.Table;
import com.hzyi.jplab.core.util.CoordinateSystem;
import java.util.List;
import java.util.Map;

public interface KinematicModel {

  String name();

  CoordinateSystem bodyCoordinateSystem();

  Map<String, Object> pack();

  KinematicModel unpack(Map<String, ?> map);

  List<String> codependentFields();

  Table<String, String, Double> codependentMultipliers();

  default String getFieldFullName(String field) {
    return name() + "." + field;
  }

  default String getFieldFullName(KinematicModel model, String field) {
    return model.name() + "." + field;
  }

  default String getConstantFieldName() {
    return "CONST";
  }
}
