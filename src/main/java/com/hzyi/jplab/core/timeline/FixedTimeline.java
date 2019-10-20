package com.hzyi.jplab.core.timeline;

import org.threeten.bp.Instant;
import lombok.Builder;
import lombok.Singular;
import java.util.BiFunction;

@Builder(builderMethodName = "newBuilder")
public class FixedTimeline implements Timeline {

  private final initialAssembly;
  @Singular private final Table<String, Field, BiFunction<Assembly, Double, Double>> functions;

  public Assembly getAssemblySnapshot(Instant timestamp) {
    Assembly snapshot = initialAssembly.getSnapshot();
    for (Cell<String, Field, BiFunction<Assembly, Double, Double> cell : functions.cellSet()) {
      Component component = snapshot.getComponent(cell.getRowKey());
      component.update(cell.getColumnKey(), cell.getValue().apply(initialAssembly, timestamp));
    }
    return snapshot;
  }

}