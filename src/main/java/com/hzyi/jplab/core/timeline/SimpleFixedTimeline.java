package com.hzyi.jplab.core.timeline;

import com.hzyi.jplab.core.model.AssemblySnapshot;
import java.util.function.BiFunction;

public class SimpleFixedTimeline implements Timeline {

  private final AssemblySnapshot initialAssemblySnapshot;
  private final BiFunction<AssemblySnapshot, Double, AssemblySnapshot> function;

  public SimpleFixedTimeline(
      AssemblySnapshot initialAssemblySnapshot,
      BiFunction<AssemblySnapshot, Double, AssemblySnapshot> function) {
    this.initialAssemblySnapshot = initialAssemblySnapshot;
    this.function = function;
  }

  public AssemblySnapshot getAssemblySnapshot(double timestamp) {
    return function.apply(initialAssemblySnapshot, timestamp);
  }
}
