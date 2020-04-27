package com.hzyi.jplab.core.model.kinematic;

import com.google.common.collect.Table;

public interface Field {

  String name();

  Table<String, String, Double> codependentMultipliers();
}
