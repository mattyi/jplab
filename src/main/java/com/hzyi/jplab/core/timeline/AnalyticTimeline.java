package com.hzyi.jplab.core.timeline;

import com.hzyi.jplab.core.model.Assembly;
import java.util.function.BiFunction;

public class AnalyticTimeline implements Timeline {

  private static final double DEFAULT_TIMESTEP = 0.02;

  private final Assembly initialAssembly;
  private final BiFunction<Assembly, Double, Assembly> function;
  private double latestTimestamp = 0.0;

  public AnalyticTimeline(
      Assembly initialAssembly, BiFunction<Assembly, Double, Assembly> function) {
    this.initialAssembly = initialAssembly;
    this.function = function;
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
  public Assembly getLatestAssembly() {
    return function.apply(initialAssembly, latestTimestamp);
  }
}
