package com.hzyi.jplab.dum;

import com.hzyi.jplab.application.BaseApplication;
import com.hzyi.jplab.controller.Controller;
import com.hzyi.jplab.controller.Parameter;
import com.hzyi.jplab.controller.Observer;
import com.hzyi.jplab.model.assembly.Assembly;
import com.hzyi.jplab.model.component.CircMassPoint;
import com.hzyi.jplab.solver.Solver;

public class SingleCircApplication extends BaseApplication {

  public static void main(String[] args) {
    launch(args);
  }

  protected String initializeApplicationName() {
    return "Single Circle Application";
  }

  protected Assembly initializeAssembly() {
    CircMassPoint circ = CircMassPoint.newBuilder()
        .setName("circ")
        .setX(0.0)
        .setY(0.0)
        .setDirX(1.0)
        .setDirY(0.0)
        .setVX(0.0)
        .setVY(0.0)
        .setMass(1.0)
        .setMomentOfInertia(1.0)
        .setRadius(0.2)
        .build();
    Assembly assembly = Assembly.newBuilder()
        .setName("assembly")
        .add(circ)
        .build();
    return assembly;
  }

  protected Solver initializeSolver() {
    return null;
  }

  protected Controller initializeController() {
    Controller controller = Controller.newController();
    Parameter<Double> circRadiusParameter = Parameter.newDoubleParameter(0.2, "circ radius");
    circRadiusParameter.addObserver(new Observer<Double>(){
      @Override
      public void update(Double v) {
        // assembly.getComponent("circ").setRadius(); // add setRadius
      }
    });
    controller.addParameter(circRadiusParameter);
    return controller;
  }
}