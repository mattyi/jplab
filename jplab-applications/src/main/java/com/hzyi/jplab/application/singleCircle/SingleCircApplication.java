package com.hzyi.jplab.application.singlecircle;

import com.hzyi.jplab.core.application.Application;
import com.hzyi.jplab.core.application.UIWrapper;
import com.hzyi.jplab.core.controller.Controller;
import com.hzyi.jplab.core.controller.IntervalDoubleParameter;
import com.hzyi.jplab.core.controller.Observer;
import com.hzyi.jplab.core.controller.Parameter;
import com.hzyi.jplab.core.model.Assembly;
import com.hzyi.jplab.core.model.CircMassPoint;
import com.hzyi.jplab.core.solver.Solver;
import com.hzyi.jplab.core.viewer.CoordinateTransformer;
import com.hzyi.jplab.core.viewer.PainterFactory;
import com.hzyi.jplab.core.viewer.CirclePainter;
import javafx.scene.canvas.Canvas;
import com.hzyi.jplab.core.viewer.Appearance;

public class SingleCircApplication {

  public static void main(String[] args) {
    String name = "Single Circle Application";
    Solver solver = initializeSolver();
    Controller controller = initializeController();
    PainterFactory painterFactory = initializePainterFactory();
    Assembly assembly = initializeAssembly(painterFactory);
    Application application =
        Application.newBuilder()
            .name(name)
            .assembly(assembly)
            .solver(solver)
            .controller(controller)
            .painterFactory(painterFactory)
            .build();
    UIWrapper.setApplication(application);
    UIWrapper.startSimulation();
  }

  protected String initializeApplicationName() {
    return "Single Circle Application";
  }

  private static Assembly initializeAssembly(PainterFactory painterFactory) {
    CircMassPoint circ =
        CircMassPoint.newBuilder()
            .name("circ")
            .x(20.0)
            .y(0.0)
            .vx(0.0)
            .vy(0.0)
            .mass(1.0)
            .radius(20)
            .appearance(Appearance.of())
            .build();
    Assembly assembly = Assembly.newBuilder().name("assembly").component("circ", circ).painterFactory(painterFactory).build();
    assembly.applySelfToComponents();
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

  private static PainterFactory initializePainterFactory() {
    Canvas canvas = new Canvas(400, 400);
    double ratio = 1;
    return new PainterFactory(canvas, new CoordinateTransformer(canvas, ratio));
  }
}
