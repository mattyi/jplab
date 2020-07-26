package com.hzyi.jplab.core.application;

import com.hzyi.jplab.core.application.config.ApplicationConfig;
import com.hzyi.jplab.core.application.ui.PrimaryStageFactory;
import com.hzyi.jplab.core.controller.Controller;
import com.hzyi.jplab.core.model.Assembly;
import com.hzyi.jplab.core.painter.CoordinateTransformer;
import com.hzyi.jplab.core.painter.PainterFactory;
import com.hzyi.jplab.core.timeline.Timeline;
import java.util.Timer;
import java.util.TimerTask;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import lombok.Getter;

public class Application extends javafx.application.Application {

  @Getter private static String name;
  @Getter private static Assembly initialAssembly;
  @Getter private static Controller controller;
  @Getter private static Timeline timeline;
  @Getter private static PainterFactory painterFactory;
  @Getter private static double refreshPeriod;
  @Getter private static Canvas canvas;
  @Getter private static CoordinateTransformer coordinateTransformer;

  private static boolean isInitialized;

  public static void load(ApplicationConfig config) {
    ApplicationFactory.newApp(config);
  }

  public static void init(
      String name,
      Assembly initialAssembly,
      Controller controller,
      Canvas canvas,
      CoordinateTransformer transformer,
      PainterFactory painterFactory,
      Timeline timeline,
      double refreshPeriod) {
    if (isInitialized) {
      throw new IllegalStateException("application initialized twice");
    }
    Application.name = name;
    Application.initialAssembly = initialAssembly;
    Application.controller = controller;
    Application.canvas = canvas;
    Application.coordinateTransformer = transformer;
    Application.painterFactory = painterFactory;
    Application.timeline = timeline;
    Application.refreshPeriod = refreshPeriod;
    Application.isInitialized = true;
  }

  public static void reset() {
    Application.name = null;
    Application.initialAssembly = null;
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
            Application.getCanvas());
    primaryStage.show();

    TimerTask task =
        new TimerTask() {
          private double nextRefreshThreshold = 0;

          @Override
          public void run() {
            timeline.getLatestAssembly().paint();
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
