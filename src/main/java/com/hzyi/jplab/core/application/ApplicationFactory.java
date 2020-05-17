package com.hzyi.jplab.core.application;

import static com.google.common.base.Preconditions.checkArgument;

import com.hzyi.jplab.core.application.config.ApplicationConfig;
import com.hzyi.jplab.core.controller.Controller;
import com.hzyi.jplab.core.model.Assembly;
import com.hzyi.jplab.core.model.AssemblySnapshot;
import com.hzyi.jplab.core.model.Component;
import com.hzyi.jplab.core.model.InstantiatingComponent;
import com.hzyi.jplab.core.model.kinematic.ConnectingModel;
import com.hzyi.jplab.core.model.kinematic.Field;
import com.hzyi.jplab.core.model.kinematic.GravityField;
import com.hzyi.jplab.core.model.kinematic.KinematicModel;
import com.hzyi.jplab.core.model.kinematic.MassPoint;
import com.hzyi.jplab.core.model.kinematic.RodModel;
import com.hzyi.jplab.core.model.kinematic.SingleKinematicModel;
import com.hzyi.jplab.core.model.kinematic.SpringModel;
import com.hzyi.jplab.core.model.kinematic.StaticModel;
import com.hzyi.jplab.core.model.shape.Appearance;
import com.hzyi.jplab.core.model.shape.Circle;
import com.hzyi.jplab.core.model.shape.Edge;
import com.hzyi.jplab.core.model.shape.Line;
import com.hzyi.jplab.core.model.shape.Shape;
import com.hzyi.jplab.core.model.shape.ZigzagLine;
import com.hzyi.jplab.core.painter.CirclePainter;
import com.hzyi.jplab.core.painter.CoordinateTransformer;
import com.hzyi.jplab.core.painter.EdgePainter;
import com.hzyi.jplab.core.painter.LinePainter;
import com.hzyi.jplab.core.painter.PainterFactory;
import com.hzyi.jplab.core.painter.ZigzagLinePainter;
import com.hzyi.jplab.core.timeline.NumericTimeline;
import com.hzyi.jplab.core.timeline.Timeline;
import java.util.List;
import java.util.Map;
import javafx.scene.canvas.Canvas;

public class ApplicationFactory {

  private ApplicationFactory() {}

  public static void newApp(ApplicationConfig config) {
    String name = config.getName();
    Controller controller = null;
    PainterFactory painterFactory = createPainterFactory(config.getCanvas());
    Assembly assembly = createAssembly(config.getAssembly(), config.getFields(), painterFactory);
    Timeline timeline = createTimeline(assembly.getInitialAssemblySnapshot(), config.getTimeline());
    Application.init(
        name, assembly, controller, painterFactory, timeline, config.getRefreshPeriod());
  }

  private static Timeline createTimeline(
      AssemblySnapshot initialSnapshot, ApplicationConfig.TimelineConfig timelineConfig) {
    switch (timelineConfig.getType()) {
      case NUMERIC:
        return new NumericTimeline(
            initialSnapshot, (Double) timelineConfig.getTimelineSpecs().get("time_step"));
      case FIXED:
        throw new UnsupportedOperationException("fixed timeline not implemented.");
    }
    return null;
  }

  private static PainterFactory createPainterFactory(ApplicationConfig.CanvasConfig canvasConfig) {
    Canvas canvas = new Canvas(canvasConfig.getWidth(), canvasConfig.getHeight());
    double ratio = canvasConfig.getNaturalScreenRatio();
    return new PainterFactory(canvas, new CoordinateTransformer(canvas, ratio));
  }

  private static Assembly createAssembly(
      List<ApplicationConfig.ComponentConfig> componentConfigs,
      List<ApplicationConfig.FieldConfig> fieldConfigs,
      PainterFactory painterFactory) {
    Assembly assembly = Assembly.getInstance();
    for (ApplicationConfig.ComponentConfig config : componentConfigs) {
      if (config.getKinematicModel().getType().isSingleModel()) {
        assembly.withComponent(createComponent(config, painterFactory, assembly));
      }
    }
    for (ApplicationConfig.ComponentConfig config : componentConfigs) {
      if (config.getKinematicModel().getType().isConnector()) {
        assembly.withComponent(createComponent(config, painterFactory, assembly));
      }
    }

    for (ApplicationConfig.FieldConfig config : fieldConfigs) {
      assembly.withField(createField(config));
    }
    return assembly;
  }

  private static Component createComponent(
      ApplicationConfig.ComponentConfig componentConfig,
      PainterFactory painterFactory,
      Assembly assembly) {
    Shape shape = createShape(componentConfig.getShape());
    KinematicModel model =
        createKinematicModel(
            componentConfig.getKinematicModel(), componentConfig.getName(), assembly);
    Appearance appearance = Appearance.unpack(componentConfig.getAppearance());
    Shape.Type shapeType = componentConfig.getShape().getType();
    KinematicModel.Type kinematicModelType = componentConfig.getKinematicModel().getType();

    if (shapeType == Shape.Type.CIRCLE && kinematicModelType == KinematicModel.Type.MASS_POINT) {
      return new InstantiatingComponent<SingleKinematicModel, Circle, CirclePainter>(
          componentConfig.getName(),
          (MassPoint) model,
          (Circle) shape,
          painterFactory.getCirclePainter(),
          appearance);
    } else if (shapeType == Shape.Type.EDGE
        && kinematicModelType == KinematicModel.Type.STATIC_MODEL) {
      return new InstantiatingComponent<StaticModel, Edge, EdgePainter>(
          componentConfig.getName(),
          (StaticModel) model,
          (Edge) shape,
          painterFactory.getEdgePainter(),
          appearance);
    } else if (shapeType == Shape.Type.ZIGZAG_LINE
        && kinematicModelType == KinematicModel.Type.SPRING_MODEL) {
      return new InstantiatingComponent<ConnectingModel, ZigzagLine, ZigzagLinePainter>(
          componentConfig.getName(),
          (ConnectingModel) model,
          (ZigzagLine) shape,
          painterFactory.getZigzagLinePainter(),
          appearance);
    } else if (shapeType == Shape.Type.LINE
        && kinematicModelType == KinematicModel.Type.ROD_MODEL) {
      return new InstantiatingComponent<ConnectingModel, Line, LinePainter>(
          componentConfig.getName(),
          (ConnectingModel) model,
          (Line) shape,
          painterFactory.getLinePainter(),
          appearance);
    }
    checkArgument(
        false,
        "unsupported combination of shape and kinematic model: %s, %s",
        shapeType,
        kinematicModelType);
    return null;
  }

  public static Shape createShape(ApplicationConfig.ComponentConfig.ShapeConfig shapeConfig) {
    switch (shapeConfig.getType()) {
      case CIRCLE:
        return Circle.unpack(shapeConfig.getShapeSpecs());
      case ZIGZAG_LINE:
        return ZigzagLine.unpack(shapeConfig.getShapeSpecs());
      case EDGE:
        return Edge.unpack(shapeConfig.getShapeSpecs());
      case LINE:
        return Line.unpack(shapeConfig.getShapeSpecs());
      default:
        checkArgument(false, "unsupported shape type: %s", shapeConfig.getType());
    }
    return null;
  }

  private static KinematicModel createKinematicModel(
      ApplicationConfig.ComponentConfig.KinematicModelConfig config,
      String componentName,
      Assembly assembly) {
    Map<String, Object> specs = config.getKinematicModelSpecs();
    specs.put("name", componentName);
    switch (config.getType()) {
      case MASS_POINT:
        return MassPoint.of(specs);
      case SPRING_MODEL:
        specs.put("_assembly_snapshot", assembly.getInitialAssemblySnapshot());
        return SpringModel.of(specs);
      case STATIC_MODEL:
        return StaticModel.of(specs);
      case ROD_MODEL:
        specs.put("_assembly_snapshot", assembly.getInitialAssemblySnapshot());
        return RodModel.of(specs);
      default:
        checkArgument(false, "unsupported kinematic model type: %s", config.getType());
    }
    return null;
  }

  private static Field createField(ApplicationConfig.FieldConfig config) {
    if (config.getType() == ApplicationConfig.FieldConfig.Type.GRAVITY) {
      return GravityField.of(config.getFieldSpecs());
    }
    throw new UnsupportedOperationException("Only gravity field is supported.");
  }
}
