package com.hzyi.jplab.core.application;

import com.hzyi.jplab.core.controller.Controller;
import com.hzyi.jplab.core.model.Assembly;
import com.hzyi.jplab.core.painter.PainterFactory;
import com.hzyi.jplab.core.solver.Solver;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Builder(builderMethodName = "newBuilder")
@Accessors(fluent = true)
public class Application {

  @Getter private String name;
  @Getter private Assembly assembly;
  @Getter private Solver solver;
  @Getter private Controller controller;
  @Getter private PainterFactory painterFactory;

  public void start() {
    assembly().paint();
  }
}
