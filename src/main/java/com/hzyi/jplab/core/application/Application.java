package com.hzyi.jplab.core.application;

import com.hzyi.jplab.core.application.config.ApplicationConfig;
import com.hzyi.jplab.core.application.ui.PrimaryStageFactory;
import com.hzyi.jplab.core.controller.Controller;
import com.hzyi.jplab.core.model.Assembly;
import com.hzyi.jplab.core.model.AssemblySnapshot;
import com.hzyi.jplab.core.painter.PainterFactory;
import com.hzyi.jplab.core.timeline.Timeline;
import java.util.Timer;
import java.util.TimerTask;
import javafx.stage.Stage;
import lombok.Getter;

public class Application extends javafx.application.Application {

  @Getter private static String name;
  @Getter private static Assembly assembly;
  @Getter private static Controller controller;
  @Getter private static Timeline timeline;
  @Getter private static PainterFactory painterFactory;
  @Getter private static double refreshPeriod;

  private static boolean isInitialized;

  public static void load(ApplicationConfig config) {
    ApplicationFactory.newApp(config);
  }

  public static void init(
      String name,
      Assembly assembly,
      Controller controller,
      PainterFactory painterFactory,
      Timeline timeline,
      double refreshPeriod) {
    if (isInitialized) {
      throw new IllegalStateException("application initialized twice");
    }
    Application.name = name;
    Application.assembly = assembly;
    Application.controller = controller;
    Application.painterFactory = painterFactory;
    Application.timeline = timeline;
    Application.refreshPeriod = refreshPeriod;
    Application.isInitialized = true;
    System.out.println("Application is initialized");
  }

  public static void reset() {
    Application.name = null;
    Application.assembly = null;
    Application.controller = null;
    Application.painterFactory = null;
    Application.timeline = null;
    Application.refreshPeriod = 0.0;
    Application.isInitialized = false;
  }

  public static void run() {
    launch();
  }

  @Override
  public void start(Stage primaryStage) {
    primaryStage =
        PrimaryStageFactory.initPrimaryStage(
            primaryStage,
            Application.getName(),
            Application.getController(),
            Application.getPainterFactory());
    primaryStage.show();

    TimerTask task =
        new TimerTask() {
          private double nextRefreshThreshold = 0;

          @Override
          public void run() {
            AssemblySnapshot snapshot = timeline.getLatestAssemblySnapshot();
            assembly.paint(snapshot);
            nextRefreshThreshold += refreshPeriod;
            while (timeline.getTimestamp() < nextRefreshThreshold) {
              timeline.advance();
            }
          }
        };

    new Timer().schedule(task, 100L, (long) (refreshPeriod * 1000));
  }

  public static void startSimulation() {
    launch();
  }
}
