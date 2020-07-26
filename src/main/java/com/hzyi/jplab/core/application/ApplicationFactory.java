package com.hzyi.jplab.core.application;

import static com.google.common.base.Preconditions.checkArgument;

import com.hzyi.jplab.core.application.config.ApplicationConfig;
import com.hzyi.jplab.core.controller.Controller;
import com.hzyi.jplab.core.model.Assembly;
import com.hzyi.jplab.core.model.kinematic.Connector;
import com.hzyi.jplab.core.model.kinematic.Field;
import com.hzyi.jplab.core.model.kinematic.GravityField;
import com.hzyi.jplab.core.model.kinematic.MassPoint;
import com.hzyi.jplab.core.model.kinematic.RodModel;
import com.hzyi.jplab.core.model.kinematic.RopeModel;
import com.hzyi.jplab.core.model.kinematic.SingleKinematicModel;
import com.hzyi.jplab.core.model.kinematic.SpringModel;
import com.hzyi.jplab.core.model.kinematic.StaticModel;
import com.hzyi.jplab.core.painter.CoordinateTransformer;
import com.hzyi.jplab.core.painter.PainterFactory;
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
    Canvas canvas = createCanvas(config.getCanvas());
    CoordinateTransformer transformer = createCoordinateTransformer(canvas, config.getCanvas());
    PainterFactory painterFactory = createPainterFactory(canvas, transformer);
    Assembly initialAssembly =
        createInitialAssembly(
            config.getKinematicModels(),
            config.getConnectors(),
            config.getFields(),
            painterFactory);
    Timeline timeline = createTimeline(initialAssembly, config.getTimeline());
    Application.init(
        name,
        initialAssembly,
        controller,
        canvas,
        transformer,
        painterFactory,
        timeline,
        config.getRefreshPeriod());
  }

  private static Timeline createTimeline(
      Assembly initialAssembly, ApplicationConfig.TimelineConfig timelineConfig) {
    switch (timelineConfig.getType()) {
      case NUMERIC:
        return new NumericTimeline(
            initialAssembly, (Double) timelineConfig.getTimelineSpecs().get("time_step"));
      case FIXED:
        throw new UnsupportedOperationException("fixed timeline not implemented.");
    }
    return null;
  }

  private static Canvas createCanvas(ApplicationConfig.CanvasConfig canvasConfig) {
    return new Canvas(canvasConfig.getWidth(), canvasConfig.getHeight());
  }

  private static CoordinateTransformer createCoordinateTransformer(
      Canvas canvas, ApplicationConfig.CanvasConfig canvasConfig) {
    return new CoordinateTransformer(canvas, canvasConfig.getNaturalScreenRatio());
  }

  private static PainterFactory createPainterFactory(
      Canvas canvas, CoordinateTransformer transformer) {
    return new PainterFactory(canvas, transformer);
  }

  private static Assembly createInitialAssembly(
      List<ApplicationConfig.KinematicModelConfig> kinematicModelConfigs,
      List<ApplicationConfig.ConnectorConfig> connectorConfigs,
      List<ApplicationConfig.FieldConfig> fieldConfigs,
      PainterFactory painterFactory) {
    Assembly assembly = Assembly.empty();
    for (ApplicationConfig.KinematicModelConfig config : kinematicModelConfigs) {
      assembly.withComponent(createKinematicModel(config));
    }
    for (ApplicationConfig.ConnectorConfig config : connectorConfigs) {
      assembly.withComponent(createConnector(config, assembly));
    }

    for (ApplicationConfig.FieldConfig config : fieldConfigs) {
      assembly.withComponent(createField(config));
    }
    return assembly;
  }

  private static SingleKinematicModel createKinematicModel(
      ApplicationConfig.KinematicModelConfig config) {
    switch (config.getType()) {
      case MASS_POINT:
        return MassPoint.of(config);
      case STATIC_MODEL:
        return StaticModel.of(config);
      default:
        checkArgument(false, "unsupported kinematic model type: %s", config.getType());
    }
    return null;
  }

  private static Connector createConnector(
      ApplicationConfig.ConnectorConfig config, Assembly assembly) {
    Map<String, Object> specs = config.getConnectorSpecs();
    specs.put("_assembly", assembly);
    switch (config.getType()) {
      case SPRING_MODEL:
        return SpringModel.of(config);
      case ROD_MODEL:
        return RodModel.of(config);
      case ROPE_MODEL:
        return RopeModel.of(config);
      default:
        checkArgument(false, "unsupported connector type: %s", config.getType());
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
