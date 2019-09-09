package com.hzyi.jplab.core.application.ui;

import com.hzyi.jplab.core.controller.IntervalDoubleParameter;
import com.hzyi.jplab.core.controller.Parameter;
import javafx.scene.control.Slider;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

public class ParameterPaneFactory {

  private ParameterPaneFactory() {
    // utility class
  }

  public static Pane newParameterPane(Parameter parameter) {
    FlowPane container = new FlowPane();
    container.getChildren().add(newParameterNameLabel(parameter));
    container.getChildren().add(newParameterSlider(parameter));
    return container;
  }

  private static Label newParameterNameLabel(Parameter parameter) {
    return new Label(parameter.getName());
  }

  private static Slider newParameterSlider(Parameter parameter) {
    if (parameter instanceof IntervalDoubleParameter) {
      return newIntervalDoubleParameterSlider((IntervalDoubleParameter) parameter);
    } else {
      throw new IllegalArgumentException("Parameter type currently not supported");
    } 
  }

  private static Slider newIntervalDoubleParameterSlider(IntervalDoubleParameter parameter) {
    Slider slider = new Slider();
    slider.setMin(parameter.getMin());
    slider.setMax(parameter.getMax());
    slider.setValue(parameter.getValue());
    slider.setShowTickLabels(true);
    slider.setShowTickMarks(true);
    slider.setMajorTickUnit(parameter.getInterval());
    sliderListener(parameter, slider);
    return slider;
  }

  private static void sliderListener(IntervalDoubleParameter parameter, Slider slider) {
    slider.valueProperty().addListener(
        (obs, oldVal, newVal) -> {
          parameter.setValue(newVal.doubleValue());
        });
  }
}