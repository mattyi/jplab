package com.hzyi.jplab.core.application;

import com.hzyi.jplab.core.application.ui.ParameterPaneProvider;
import com.hzyi.jplab.core.controller.Controller;
import com.hzyi.jplab.core.controller.IntervalDoubleParameter;
import com.hzyi.jplab.core.controller.Parameter;
import com.hzyi.jplab.core.model.Assembly;
import com.hzyi.jplab.core.solver.Solver;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.FlowPane;
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
    row2.setPercentHeight(80);
    RowConstraints row3 = new RowConstraints();
    row3.setPercentHeight(10);
    grid.getColumnConstraints().add(column1);
    grid.getColumnConstraints().add(column2);
    grid.getRowConstraints().add(row1);
    grid.getRowConstraints().add(row2);
    grid.getRowConstraints().add(row3);
    Text applicationNameText = new Text(applicationName);
    Pane canvas = new Pane();
    canvas.setStyle("-fx-background-color: black;");
    ScrollPane controllerPane = initializeControllerPane();
    grid.add(applicationNameText, 0, 0);
    grid.add(canvas, 0, 1, 1, 2);
    grid.add(controllerPane, 1, 0, 1, 2);
    grid.add(new Text("text"), 1, 2, 1, 1);
    Scene scene = new Scene(grid, 800, 800);
    primaryStage.setScene(scene);
  }

  private ScrollPane initializeControllerPane() {
    ScrollPane controllerPane = new ScrollPane();
    ParameterPaneProvider provider = new ParameterPaneProvider();
    FlowPane container = new FlowPane();
    // Slider slider = new Slider();
    // slider.setMin(0);
    // slider.setMax(100);
    // slider.setValue(40);
    // slider.setShowTickLabels(true);
    // slider.setShowTickMarks(true);
    // slider.setMajorTickUnit(50);
    // slider.setMinorTickCount(5);
    // slider.setBlockIncrement(10);
    // container.getChildren().add(provider.createParameterPane());
    for (Parameter parameter : this.controller.getParameters()) {
      if (parameter instanceof IntervalDoubleParameter) {
        IntervalDoubleParameter p = (IntervalDoubleParameter)parameter;
        provider.setParameter(p);
        container.getChildren().add(provider.createParameterPane());
      }
    }
    System.out.println(container.getChildren().size());
    controllerPane.setContent(container);
    return controllerPane;
  }
}