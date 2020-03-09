package com.hzyi.jplab.core.timeline;

import com.hzyi.jplab.core.model.AssemblySnapshot;
import com.hzyi.jplab.core.model.kinematic.KinematicModel;
import java.util.Map;
import java.util.function.BiFunction;
import lombok.Builder;
import lombok.Singular;

@Builder(builderMethodName = "newBuilder")
public class FixedTimeline implements Timeline {

  private final AssemblySnapshot initialAssemblySnapshot;

  @Singular
  private final Map<String, BiFunction<AssemblySnapshot, Double, KinematicModel>> functions;

  public AssemblySnapshot getAssemblySnapshot(double timestamp) {
    AssemblySnapshot.Builder snapshot = initialAssemblySnapshot.toBuilder();
    for (Map.Entry<String, BiFunction<AssemblySnapshot, Double, KinematicModel>> entry :
        functions.entrySet()) {
      snapshot.set(entry.getKey(), entry.getValue().apply(initialAssemblySnapshot, timestamp));
    }
    return snapshot.build();
  }
}
