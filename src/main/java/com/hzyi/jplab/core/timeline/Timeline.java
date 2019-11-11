package com.hzyi.jplab.core.timeline;

import com.hzyi.jplab.core.model.AssemblySnapshot;

public interface Timeline {

  AssemblySnapshot getAssemblySnapshot(double timestamp);
}
