package com.hzyi.jplab.core.timeline;

import com.hzyi.jplab.core.model.Assembly;
import com.hzyi.jplab.core.util.DictionaryMatrix;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/** A timeline that updates Assembly through numeric calculations. */
@EqualsAndHashCode
public class NumericTimeline implements Timeline {

  private final Assembly initialAssembly;
  @Getter private final double timeStep;
  @Getter private double timestamp;
  @Getter private Assembly latestAssembly;

  public NumericTimeline(Assembly initialAssembly) {
    this(initialAssembly, 0.02);
  }

  public NumericTimeline(Assembly initialAssembly, double timeStep) {
    this.initialAssembly = initialAssembly;
    this.latestAssembly = initialAssembly;
    this.timeStep = timeStep;
  }

  @Override
  public void advance(double timeStep) {
    Transaction txn = new Transaction();
    for (Verifier v : latestAssembly.getVerifiers()) {
      txn.withVerifier(v);
    }
    latestAssembly =
        txn.run(
            latestAssembly,
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
