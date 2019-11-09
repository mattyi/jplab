package com.hzyi.jplab.core.application;

import com.hzyi.jplab.core.application.ui.PrimaryStageFactory;
import javafx.stage.Stage;

public class UIWrapper extends javafx.application.Application {

  private static Application application;

  public static void setApplication(Application app) {
    application = app;
  }

  @Override
  public void start(Stage primaryStage) {
    primaryStage =
        PrimaryStageFactory.initPrimaryStage(
            primaryStage,
            application.name(),
            application.controller(),
            application.painterFactory());
    primaryStage.show();
    application.start();
  }

  public static void startSimulation() {
    launch();
  }
}
