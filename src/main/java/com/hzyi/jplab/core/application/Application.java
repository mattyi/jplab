package com.hzyi.jplab.core.application;

import com.hzyi.jplab.core.application.config.ApplicationConfig;
import com.hzyi.jplab.core.application.exceptions.ResourceExhaustedException;
import com.hzyi.jplab.core.application.ui.PrimaryStageFactory;
import com.hzyi.jplab.core.controller.Controller;
import com.hzyi.jplab.core.model.Assembly;
import com.hzyi.jplab.core.painter.CoordinateTransformer;
import com.hzyi.jplab.core.painter.PainterFactory;
import com.hzyi.jplab.core.timeline.Timeline;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
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
    ScheduledExecutorService exec = Executors.newScheduledThreadPool(4);

    Runnable task =
        new Runnable() {
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

    Callable<Future> taskRunner =
        new Callable() {
          @Override
          public Future call() {
            return exec.submit(task);
          }
        };

    exec.scheduleAtFixedRate(
        new Runnable() {
          private Future previousTask;

          public void run() {
            if (previousTask != null && !previousTask.isDone()) {
              System.out.println(new ResourceExhaustedException());
              previousTask.cancel(true);
              exec.shutdownNow();
            }
            try {
              previousTask = taskRunner.call();
            } catch (Exception e) {
              System.out.println(e);
              exec.shutdownNow();
            }
          }
        },
        0,
        (long) (refreshPeriod * 1000),
        TimeUnit.MILLISECONDS);
  }

  public static void startSimulation() {
    launch();
  }
}
