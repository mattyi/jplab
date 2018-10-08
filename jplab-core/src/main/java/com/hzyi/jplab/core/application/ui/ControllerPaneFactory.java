package com.hzyi.jplab.core.application.ui;

import com.hzyi.jplab.core.controller.Controller;
import com.hzyi.jplab.core.controller.IntervalDoubleParameter;
import com.hzyi.jplab.core.controller.Parameter;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

public class ControllerPaneFactory {

  private ControllerPaneFactory() {
    // utility class
  }

  public static ScrollPane newControllerPane(Controller controller) {
    ScrollPane controllerPane = new ScrollPane();
    FlowPane container = new FlowPane();
    for (Parameter parameter : controller.getParameters()) {
      if (parameter instanceof IntervalDoubleParameter) {
        IntervalDoubleParameter p = (IntervalDoubleParameter)parameter;
        container.getChildren().add(ParameterPaneFactory.newParameterPane(p));
      }
    }
    controllerPane.setContent(container);
    return controllerPane;
  }
}