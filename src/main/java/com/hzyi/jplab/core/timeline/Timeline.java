package com.hzyi.jplab.core.timeline;

import com.hzyi.jplab.core.model.AssemblySnapshot;

public interface Timeline extends AdvancingTimeline {

  AssemblySnapshot getAssemblySnapshot(double timestamp);
}
