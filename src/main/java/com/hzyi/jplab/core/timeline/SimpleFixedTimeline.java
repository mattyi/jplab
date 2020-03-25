package com.hzyi.jplab.core.timeline;

import com.hzyi.jplab.core.model.AssemblySnapshot;
import java.util.function.BiFunction;

public class SimpleFixedTimeline implements Timeline {

  private final AssemblySnapshot initialAssemblySnapshot;
  private final BiFunction<AssemblySnapshot, Double, AssemblySnapshot> function;
  private double latestTimestamp = 0;

  public SimpleFixedTimeline(
      AssemblySnapshot initialAssemblySnapshot,
      BiFunction<AssemblySnapshot, Double, AssemblySnapshot> function) {
    this.initialAssemblySnapshot = initialAssemblySnapshot;
    this.function = function;
  }

  @Override
  public AssemblySnapshot getAssemblySnapshot(double timestamp) {
    return function.apply(initialAssemblySnapshot, timestamp);
  }

  @Override
  public void advance(double timeStep) {
    latestTimestamp += timeStep;
  }

  @Override
  public AssemblySnapshot getLatestAssemblySnapshot() {
    return function.apply(initialAssemblySnapshot, latestTimestamp);
  }
}
