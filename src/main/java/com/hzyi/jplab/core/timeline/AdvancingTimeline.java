package com.hzyi.jplab.core.timeline;

import com.hzyi.jplab.core.model.AssemblySnapshot;

public interface AdvancingTimeline {

  AssemblySnapshot getLatestAssemblySnapshot();

  void advance(double timeStep);

}
