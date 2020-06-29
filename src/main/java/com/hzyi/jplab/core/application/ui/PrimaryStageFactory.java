package com.hzyi.jplab.core.application.ui;

import com.hzyi.jplab.core.controller.Controller;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public final class PrimaryStageFactory {

  private PrimaryStageFactory() {
    // utility class
  }

  public static Stage initPrimaryStage(
      Stage primaryStage, String name, Controller controller, Canvas canvas) {
    GridPane grid = new GridPane();
    ColumnConstraints column1 = new ColumnConstraints();
    column1.setPercentWidth(60);
    ColumnConstraints column2 = new ColumnConstraints();
    column2.setPercentWidth(40);
    RowConstraints row1 = new RowConstraints();
    row1.setPercentHeight(10);
    RowConstraints row2 = new RowConstraints();
    row2.setPercentHeight(80);
    RowConstraints row3 = new RowConstraints();
    row3.setPercentHeight(10);
    grid.getColumnConstraints().add(column1);
    grid.getColumnConstraints().add(column2);
    grid.getRowConstraints().add(row1);
    grid.getRowConstraints().add(row2);
    grid.getRowConstraints().add(row3);

    Text applicationNameText = new Text(name);
    ScrollPane controllerPane = ControllerPaneFactory.newControllerPane(controller);

    grid.add(applicationNameText, 0, 0);
    Pane canvasPane = new Pane();
    canvasPane.getChildren().add(canvas);
    grid.add(canvasPane, 0, 1, 1, 2);
    grid.add(controllerPane, 1, 0, 1, 2);
    canvas.widthProperty().bind(canvasPane.widthProperty());
    canvas.heightProperty().bind(canvasPane.heightProperty());
    canvas.getGraphicsContext2D().setFill(Color.GREEN);
    canvas.getGraphicsContext2D().fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    grid.add(new Text("text"), 1, 2, 1, 1);

    Scene scene = new Scene(grid, 800, 800);
    primaryStage.setScene(scene);
    return primaryStage;
  }

  private Pane initializePlayPane() {
    Button playButton = new Button("play");
    Button stopButton = new Button("stop");
    return null;
  }
}
