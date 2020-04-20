package com.hzyi.jplab.core.timeline;

import com.hzyi.jplab.core.model.AssemblySnapshot;

public interface Timeline {

  AssemblySnapshot getLatestAssemblySnapshot();

  void advance(double timeStep);

  void advance();

  double getTimeStep();

  double getTimestamp();
}
