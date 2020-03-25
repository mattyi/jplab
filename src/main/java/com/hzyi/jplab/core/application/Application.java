package com.hzyi.jplab.core.application;

import com.google.common.base.Preconditions;
import com.hzyi.jplab.core.model.AssemblySnapshot;
import com.hzyi.jplab.core.timeline.AdvancingTimeline;
import java.util.Timer;
import java.util.TimerTask;
import javafx.scene.canvas.Canvas;
import lombok.Builder;
import lombok.experimental.Accessors;

@Builder(builderMethodName = "newBuilder")
@Accessors(fluent = true)
public class Application {

  private static Singleton _singleton;

  public Application() {}

  public static void loadApplicationSingleton(Singleton singleton) {
    Preconditions.checkArgument(singleton != null, "application is already initialized");
    _singleton = singleton;
  }

  public static Singleton singleton() {
    return _singleton;
  }

  public void start() {
    MutableTimestamp timestamp = new MutableTimestamp(0.0);

    TimerTask task =
        new TimerTask() {
          @Override
          public void run() {

            AdvancingTimeline timeline = _singleton.getTimeline();
            timeline.advance(0.01);
            timestamp.increment(0.01);
            AssemblySnapshot snapshot = timeline.getLatestAssemblySnapshot();
            if (timestamp.count == 10) {
              clearCanvas();
              _singleton.getAssembly().paint(snapshot);
              timestamp.count = 0;
            }
          }
        };

    new Timer().schedule(task, 100L, 10L);
  }

  private static void clearCanvas() {
    Canvas canvas = singleton().getPainterFactory().getGraphicsContext().getCanvas();
    double canvasWidth = canvas.getWidth();
    double canvasHeight = canvas.getHeight();
    singleton().getPainterFactory().getGraphicsContext().clearRect(0, 0, canvasWidth, canvasHeight);
  }

  private static class MutableTimestamp {
    double timestamp;
    int count;

    private MutableTimestamp(double timestamp) {
      this.timestamp = timestamp;
    }

    private void set(double timestamp) {
      this.timestamp = timestamp;
    }

    private void increment(double delta) {
      this.timestamp = timestamp + delta;
      count++;
    }

    private double get() {
      return this.timestamp;
    }
  }
}
