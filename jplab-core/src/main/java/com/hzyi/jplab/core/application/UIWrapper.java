package com.hzyi.jplab.core.application;

import java.util.function.Function;
import javafx.stage.Stage;

class UIWrapper extends javafx.application.Application {

  private final Function<Stage, Stage> initializer;

  UIWrapper(Function<Stage, Stage> initializer) {
    this.initializer = initializer;
  }

  @Override
  public void start(Stage primaryStage) {
    primaryStage = initializer.apply(primaryStage);
    primaryStage.show();
  }
}
