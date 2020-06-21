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
  @Getter private double timestamp;
  @Getter private AssemblySnapshot latestAssemblySnapshot;

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
    Transaction txn = new Transaction();
    for (Verifier v : latestAssemblySnapshot.getVerifiers()) {
      txn.withVerifier(v);
    }
    latestAssemblySnapshot =
        txn.run(
            latestAssemblySnapshot,
            s -> {
              DictionaryMatrix matrix = s.getCodependentMatrix(timeStep);
              return s.merge(matrix.getTableSolution());
            });
    this.timestamp += timeStep;
  }

  @Override
  public void advance() {
    advance(this.timeStep);
  }
}
