package com.hzyi.jplab.core.application;

import java.util.function.Function;
import javafx.stage.Stage;
import com.hzyi.jplab.core.application.ui.PrimaryStageFactory;

public class UIWrapper extends javafx.application.Application {

  private static Application application;


  public static void setApplication(Application app) {
    application = app;
  }

  @Override
  public void start(Stage primaryStage) {
    primaryStage = PrimaryStageFactory
                .initPrimaryStage(
                    primaryStage,
                    application.name(),
                    application.controller(),
                    application.displayer());
    primaryStage.show();
  }

  public static void startSimulation() {
    launch();
  }
}
