package com.hzyi.jplab.core.timeline;

import com.hzyi.jplab.core.model.AssemblySnapshot;
import java.util.function.BiFunction;

public class AnalyticTimeline implements Timeline {

  private static final double DEFAULT_TIMESTEP = 0.02;

  private final AssemblySnapshot initialAssemblySnapshot;
  private final BiFunction<AssemblySnapshot, Double, AssemblySnapshot> function;
  private double latestTimestamp = 0.0;

  public AnalyticTimeline(
      AssemblySnapshot initialAssemblySnapshot,
      BiFunction<AssemblySnapshot, Double, AssemblySnapshot> function) {
    this.initialAssemblySnapshot = initialAssemblySnapshot;
    this.function = function;
  }

  public AssemblySnapshot getAssemblySnapshot(double timestamp) {
    return function.apply(initialAssemblySnapshot, timestamp);
  }

  @Override
  public void advance(double timeStep) {
    latestTimestamp += timeStep;
  }

  @Override
  public void advance() {
    advance(DEFAULT_TIMESTEP);
  }

  @Override
  public double getTimestamp() {
    return latestTimestamp;
  }

  @Override
  public double getTimeStep() {
    return DEFAULT_TIMESTEP;
  }

  @Override
  public AssemblySnapshot getLatestAssemblySnapshot() {
    return function.apply(initialAssemblySnapshot, latestTimestamp);
  }
}
