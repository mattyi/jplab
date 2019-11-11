package com.hzyi.jplab.core.application;

import com.hzyi.jplab.core.controller.Controller;
import com.hzyi.jplab.core.model.Assembly;
import com.hzyi.jplab.core.painter.PainterFactory;
import com.hzyi.jplab.core.solver.Solver;
import com.hzyi.jplab.core.timeline.Timeline;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Builder(builderMethodName = "newBuilder")
@Accessors(fluent = true)
public class Application {

  @Getter private final String name;
  @Getter private final Assembly assembly;
  @Getter private final Solver solver;
  @Getter private final Controller controller;
  @Getter private final Timeline timeline;
  @Getter private final PainterFactory painterFactory;

  public void start() {
    List<Double> timestamps = new ArrayList<>();
    timestamps.add(0.0);
    new Timer()
        .schedule(
            new TimerTask() {
              @Override
              public void run() {
                assembly.clear();
                timestamps.set(0, timestamps.get(0) + 50);
                assembly.paint(timeline.getAssemblySnapshot(timestamps.get(0)));
              }
            },
            100L,
            50L);
  }
}
