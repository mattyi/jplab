package com.hzyi.jplab.core.application.ui;

import com.hzyi.jplab.core.controller.IntervalDoubleParameter;
import com.hzyi.jplab.core.controller.Parameter;
import javafx.scene.control.Slider;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

public class ParameterPaneProvider {
  
  private Parameter parameter;

  public void setParameter(Parameter parameter) {
    this.parameter = parameter;
  }

  public Parameter getParameter(Parameter parameter) {
    return this.parameter;
  }

  public Pane createParameterPane() {
    FlowPane container = new FlowPane();
    container.getChildren().add(createParameterNameLabel());
    container.getChildren().add(createParameterSlider());
    return container;
  }

  private Label createParameterNameLabel() {
    return new Label(parameter.getName());
  }

  private Slider createParameterSlider() {
    if (parameter instanceof IntervalDoubleParameter) {
      IntervalDoubleParameter idparameter = (IntervalDoubleParameter) parameter;
      Slider slider = new Slider();
      slider.setMin(idparameter.getMin());
      slider.setMax(idparameter.getMax());
      slider.setValue(idparameter.getValue());
      slider.setShowTickLabels(true);
      slider.setShowTickMarks(true);
      slider.setMajorTickUnit(idparameter.getInterval());
      //slider.setMinorTickCount(5);
      //slider.setBlockIncrement(10);
      return slider;
    } else {
      throw new IllegalArgumentException("Parameter type currently not supported");
    } 
  }
}