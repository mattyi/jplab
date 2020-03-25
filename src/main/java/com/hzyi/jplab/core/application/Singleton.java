package com.hzyi.jplab.core.application;

import com.hzyi.jplab.core.controller.Controller;
import com.hzyi.jplab.core.model.Assembly;
import com.hzyi.jplab.core.painter.PainterFactory;
import com.hzyi.jplab.core.solver.Solver;
import com.hzyi.jplab.core.timeline.AdvancingTimeline;
import lombok.Builder;
import lombok.Getter;

@Builder(builderMethodName = "newBuilder")
public class Singleton {

  @Getter private final String name;
  @Getter private final Assembly assembly;
  @Getter private final Solver solver;
  @Getter private final Controller controller;
  @Getter private final AdvancingTimeline timeline;
  @Getter private final PainterFactory painterFactory;
}
