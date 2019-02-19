package com.hzyi.jplab.dum;

import com.hzyi.jplab.core.application.Application;
import com.hzyi.jplab.core.application.UIWrapper;
import com.hzyi.jplab.core.controller.Controller;
import com.hzyi.jplab.core.controller.IntervalDoubleParameter;
import com.hzyi.jplab.core.controller.Observer;
import com.hzyi.jplab.core.controller.Parameter;
import com.hzyi.jplab.core.model.Assembly;
import com.hzyi.jplab.core.model.AssemblyState;
import com.hzyi.jplab.core.model.CircMassPoint;
import com.hzyi.jplab.core.solver.Solver;
import com.hzyi.jplab.core.viewer.CoordinateTransformer;
import com.hzyi.jplab.core.viewer.Displayer;
import com.hzyi.jplab.core.viewer.JavaFxDisplayer;
import javafx.scene.canvas.Canvas;

public class SingleCircApplication {

  public static void main(String[] args) {
    String name = "Single Circle Application";
    Assembly assembly = initializeAssembly();
    Solver solver = initializeSolver();
    Controller controller = initializeController();
    Displayer displayer = initializeDisplayer(assembly.getInitialAssemblyState());
    Application application =
        Application.newBuilder()
            .name(name)
            .assembly(assembly)
            .solver(solver)
            .controller(controller)
            .displayer(displayer)
            .build();
    UIWrapper.setApplication(application);
    UIWrapper.startSimulation();
  }

  protected String initializeApplicationName() {
    return "Single Circle Application";
  }

  private static Assembly initializeAssembly() {
    CircMassPoint circ =
        CircMassPoint.newBuilder()
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
    Assembly assembly = Assembly.newBuilder().setName("assembly").add(circ).build();
    return assembly;
  }

  private static Solver initializeSolver() {
    return new Solver() {};
  }

  private static Controller initializeController() {
    Controller controller = Controller.newController();
    Parameter<Double> circRadiusParameter =
        new IntervalDoubleParameter(0.2, "circ radius", 0.1, 1, 9);
    circRadiusParameter.addObserver(
        new Observer<Double>() {
          @Override
          public void update(Double v) {
            // assembly.getComponent("circ").setRadius(); // add setRadius
          }
        });
    controller.addParameter(circRadiusParameter);
    Parameter<Double> circMassParameter = new IntervalDoubleParameter(1.0, "circ mass", 1.0, 10, 9);
    circMassParameter.addObserver(
        new Observer<Double>() {
          @Override
          public void update(Double v) {
            // assembly.getComponent("circ").setMass(); // add setMass
          }
        });
    controller.addParameter(circMassParameter);
    return controller;
  }

  private static Displayer initializeDisplayer(AssemblyState state) {
    Canvas canvas = new Canvas();
    return JavaFxDisplayer.newBuilder()
        .setCanvas(canvas)
        .setCoordinateTransformer(new CoordinateTransformer(canvas, state))
        .build();
  }
}
