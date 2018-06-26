package com.hzyi.jplab.application;

import com.hzyi.jplab.controller.Controller;
import com.hzyi.jplab.model.assembly.Assembly;
import com.hzyi.jplab.solver.Solver;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;


public abstract class BaseApplication extends javafx.application.Application {

  protected String applicationName;

  private Stage primaryStage;
  private Assembly assembly;
  private Solver solver;
  private Controller controller;

  public static void main(String[] args) {
  	launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
  	this.primaryStage = primaryStage;
  	this.applicationName = initializeApplicationName();
    this.assembly = initializeAssembly();
    this.solver = initializeSolver();
    this.controller = initializeController();
  	initializePrimaryStage();
    primaryStage.show();
  }

  protected abstract String initializeApplicationName();

  protected abstract Assembly initializeAssembly();

  protected abstract Solver initializeSolver();

  protected abstract Controller initializeController();

  private void initializePrimaryStage() {
    GridPane grid = new GridPane();
    ColumnConstraints column1 = new ColumnConstraints();
    column1.setPercentWidth(60);
    ColumnConstraints column2 = new ColumnConstraints();
    column2.setPercentWidth(40);
    RowConstraints row1 = new RowConstraints();
    row1.setPercentHeight(10);
    RowConstraints row2 = new RowConstraints();
    row2.setPercentHeight(90);
    grid.getColumnConstraints().add(column1);
    grid.getColumnConstraints().add(column2);
    grid.getRowConstraints().add(row1);
    grid.getRowConstraints().add(row2);
    Text applicationNameText = new Text(applicationName);
    Pane canvas = new Pane();
    canvas.setStyle("-fx-background-color: black;");
    ScrollPane controllerPane = new ScrollPane();
    grid.add(applicationNameText, 0, 0);
    grid.add(canvas, 0, 1);
    grid.add(controllerPane, 1, 0, 1, 2);
    Scene scene = new Scene(grid, 800, 800);
    primaryStage.setScene(scene);
  }
  
}