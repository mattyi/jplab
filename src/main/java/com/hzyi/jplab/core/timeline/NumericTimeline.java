package com.hzyi.jplab.core.timeline;

import com.hzyi.jplab.core.model.AssemblySnapshot;
import com.hzyi.jplab.core.util.DictionaryMatrix;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/** A timeline that updates AssemblySnapshot through numeric calculations. */
@EqualsAndHashCode
public class NumericTimeline implements Timeline {

  private final AssemblySnapshot initialAssemblySnapshot;
  @Getter private final double timeStep;
  private double timestamp;
  private AssemblySnapshot latestAssemblySnapshot;

  public NumericTimeline(AssemblySnapshot initialAssemblySnapshot) {
    this(initialAssemblySnapshot, 0.02);
  }

  public NumericTimeline(AssemblySnapshot initialAssemblySnapshot, double timeStep) {
    this.initialAssemblySnapshot = initialAssemblySnapshot;
    this.latestAssemblySnapshot = initialAssemblySnapshot;
    this.timeStep = timeStep;
  }

  @Override
  public void advance(double timeStep) {
    DictionaryMatrix matrix = latestAssemblySnapshot.getCodependentMatrix(timeStep);
    latestAssemblySnapshot = latestAssemblySnapshot.merge(matrix.solve());
    timestamp += timeStep;
  }

  @Override
  public AssemblySnapshot getLatestAssemblySnapshot() {
    return latestAssemblySnapshot;
  }

  @Override
  public double getTimestamp() {
    return timestamp;
  }

  @Override
  public void advance() {
    advance(this.timeStep);
  }
}
