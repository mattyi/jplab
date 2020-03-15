package com.hzyi.jplab.core.timeline;

import com.hzyi.jplab.core.model.AssemblySnapshot;
import java.util.function.BiFunction;

public class NumericTimeline implements AdvancingTimeline {

  private final AssemblySnapshot initialAssemblySnapshot;
  private AssemblySnapshot latestAssemblySnapshot;

  public class NumericTimeline(AssemblySnapshot initialAssemblySnapshot) {
    this.initialAssemblySnapshot = initialAssemblySnapshot;
    this.latestAssemblySnapshot = initialAssemblySnapshot;
  }

  public void advance() {

  }

  public AssemblySnapshot getLatestAssemblySnapshot() {
    return latestAssemblySnapshot;
  }

  private static AssemblySnapshot advanceTimeStep(double timeStep, AssemblySnapshot snapshot) {

  }

  private static AssemblySnapshot adjustInternalState(AssemblySnapshot snapshot) {

  }

}
