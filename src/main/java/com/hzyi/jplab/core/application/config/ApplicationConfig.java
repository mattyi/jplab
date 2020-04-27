package com.hzyi.jplab.core.application.config;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

/** Represents user configuration of an application. */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class ApplicationConfig {
  /* The name of the application. */
  private String name;

  /* The config schema version. */
  private String version;

  /* The refresh period of the animation in seconds. */
  private double refreshPeriod = 0.02;

  /* The Assembly config, described in a list of components. */
  private List<ComponentConfig> assembly = new ArrayList<>();

  /* The field configs, to be merged with assembly into AssemblyConfig. */
  private List<FieldConfig> fields = new ArrayList<>();

  /* The timeline config. */
  private TimelineConfig timeline;

  /* The canvas config. Used to initialize painters as well. */
  private CanvasConfig canvas;

  @AllArgsConstructor
  @Builder
  @Data
  @NoArgsConstructor
  public static class ComponentConfig {

    /* The name of the component. */
    private String name;

    /* The initial kinematic model spec of this component. */
    private KinematicModelConfig kinematicModel;

    /* The shape of this component. */
    private ShapeConfig shape;

    /* The appearnce of this component. Including configs such as color and filling style. */
    private Map<String, Object> appearance = new HashMap<>();

    /* Extra details of the component. */
    @Singular private Map<String, Object> componentSpecs = new HashMap<>();

    @AllArgsConstructor
    @Builder
    @Data
    @NoArgsConstructor
    public static class KinematicModelConfig {

      /* The type of this kinematic model. Supported values are SPRING_MODEL, MASS_POINT and STATIC_MODEL. */
      private com.hzyi.jplab.core.model.kinematic.KinematicModel.Type type;

      /* Extra details of the kinematic model. */
      @Singular private Map<String, Object> kinematicModelSpecs = new HashMap<>();

      @JsonAnySetter
      public void setAnyProperty(String key, Object value) {
        kinematicModelSpecs.put(key, value);
      }
    }

    @AllArgsConstructor
    @Builder
    @Data
    @NoArgsConstructor
    public static class ShapeConfig {
      /* The type of the shape. Supported values are CIRCLE, ZIGZAG_LINE and EDGE. */
      private com.hzyi.jplab.core.model.shape.Shape.Type type;

      /* Extra details of the shape. */
      @Singular private Map<String, Object> shapeSpecs = new HashMap<>();

      @JsonAnySetter
      public void setAnyProperty(String key, Object value) {
        shapeSpecs.put(key, value);
      }
    }

    @JsonAnySetter
    public void setAnyProperty(String key, Object value) {
      componentSpecs.put(key, value);
    }
  }

  @AllArgsConstructor
  @Builder
  @Data
  @NoArgsConstructor
  public static class FieldConfig {

    /* The type of the physical field. Supported values include GRAVITY. */
    private Type type;

    /* Extra details of the field. */
    @Singular private Map<String, Object> fieldSpecs = new HashMap<>();

    public static enum Type {
      GRAVITY,
      UNKNOWN
    }

    @JsonAnySetter
    public void setAnyProperty(String key, Object value) {
      fieldSpecs.put(key, value);
    }
  }

  @AllArgsConstructor
  @Builder
  @Data
  @NoArgsConstructor
  public static class TimelineConfig {

    /* The type of the timeline. Supported values include FIXED and NUMERIC. */
    private Type type;

    /* Extra details of the timeline */
    @Singular private Map<String, Object> timelineSpecs = new HashMap<>();

    public static enum Type {
      NUMERIC,
      FIXED
    }

    @JsonAnySetter
    public void setAnyProperty(String key, Object value) {
      timelineSpecs.put(key, value);
    }
  }

  @AllArgsConstructor
  @Builder
  @Data
  @NoArgsConstructor
  public static class CanvasConfig {

    /* The width of the canvas. */
    private int width;

    /* The height of the canvas. */
    private int height;

    /* The rescale ratio between the natural coordinate system and screen coordinate system. */
    private double naturalScreenRatio = 1.0;
  }
}
