package com.hzyi.jplab.application;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class BaseApplication
    extends javafx.application.Application {

  protected String applicationName = "Unspecified";
  private Stage primaryStage;

  public static void main(String[] args) {
  	launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
  	this.primaryStage = primaryStage;
  	primaryStage.setTitle("title");
  	initUI();
    initialize();
    primaryStage.show();
  }

  protected abstract void initialize();
  
  private void initUI() {
    GridPane grid = new GridPane();
    Text applicationNameText = new Text(applicationName);
    Pane canvas = new Pane();
    canvas.setStyle("-fx-background-color: black;");
    ScrollPane controllerPane = new ScrollPane();
    grid.add(applicationNameText, 0, 0);
    grid.add(canvas, 0, 1);
    grid.add(controllerPane, 1, 0, 1, 2);
    Scene scene = new Scene(grid, 300, 275);
    primaryStage.setScene(scene);
  }
}