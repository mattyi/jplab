package com.hzyi.jplab.dum;

import com.hzyi.jplab.core.application.BaseApplication;
import com.hzyi.jplab.core.controller.Controller;
import com.hzyi.jplab.core.controller.IntervalDoubleParameter;
import com.hzyi.jplab.core.controller.Parameter;
import com.hzyi.jplab.core.controller.Observer;
import com.hzyi.jplab.core.model.Assembly;
import com.hzyi.jplab.core.model.CircMassPoint;
import com.hzyi.jplab.core.solver.Solver;

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
    Parameter<Double> circRadiusParameter = new IntervalDoubleParameter(0.2, "circ radius", 0.1, 1, 9);
    circRadiusParameter.addObserver(new Observer<Double>(){
      @Override
      public void update(Double v) {
        // assembly.getComponent("circ").setRadius(); // add setRadius
      }
    });
    controller.addParameter(circRadiusParameter);
    Parameter<Double> circMassParameter = new IntervalDoubleParameter(1.0, "circ mass", 1.0, 10, 9);
    circMassParameter.addObserver(new Observer<Double>() {
      @Override
      public void update(Double v) {
        // assembly.getComponent("circ").setMass(); // add setMass
      }
    });
    controller.addParameter(circMassParameter);
    return controller;
  }
}