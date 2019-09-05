package com.hzyi.jplab.core.application;

import com.google.auto.value.AutoValue;
import com.hzyi.jplab.core.application.ui.PrimaryStageFactory;
import com.hzyi.jplab.core.controller.Controller;
import com.hzyi.jplab.core.model.Assembly;
import com.hzyi.jplab.core.solver.Solver;
import javafx.scene.paint.Color;
import com.hzyi.jplab.core.viewer.PainterFactory;
import lombok.Builder;
import lombok.Getter;

@Builder
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
