package com.hzyi.jplab.core.application.config;

import static com.google.common.truth.Truth.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.hzyi.jplab.core.model.kinematic.KinematicModel;
import com.hzyi.jplab.core.model.shape.Shape;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.Test;

public class ApplicationConfigTest {

  @Test
  public void testLoadConfig() throws IOException {
    String name = "Single Circle Application";
    String version = "1";
    double refreshPeriod = 0.02;
    ApplicationConfig.ComponentConfig massPoint =
        ApplicationConfig.ComponentConfig.builder()
            .name("circ")
            .kinematicModel(
                ApplicationConfig.ComponentConfig.KinematicModelConfig.builder()
                    .type(KinematicModel.Type.MASS_POINT)
                    .kinematicModelSpec("x", 20.0)
                    .kinematicModelSpec("y", 0.0)
                    .kinematicModelSpec("vx", 0.0)
                    .kinematicModelSpec("vy", 0.0)
                    .kinematicModelSpec("mass", 10.0)
                    .build())
            .shape(
                ApplicationConfig.ComponentConfig.ShapeConfig.builder()
                    .type(Shape.Type.CIRCLE)
                    .shapeSpec("radius", 20.0)
                    .build())
            .appearance(appearanceOf())
            .build();
    ApplicationConfig.ComponentConfig wall =
        ApplicationConfig.ComponentConfig.builder()
            .name("wall")
            .kinematicModel(
                ApplicationConfig.ComponentConfig.KinematicModelConfig.builder()
                    .type(KinematicModel.Type.STATIC_MODEL)
                    .kinematicModelSpec("x", 20.0)
                    .kinematicModelSpec("y", 100.0)
                    .build())
            .shape(
                ApplicationConfig.ComponentConfig.ShapeConfig.builder()
                    .type(Shape.Type.EDGE)
                    .shapeSpec("inner_line_count", 4)
                    .shapeSpec("inner_line_height", 10.0)
                    .shapeSpec("length", 40.0)
                    .build())
            .appearance(appearanceOf("color", "RED", "line_width", 1.0))
            .build();

    ApplicationConfig.ComponentConfig spring =
        ApplicationConfig.ComponentConfig.builder()
            .name("spring")
            .kinematicModel(
                ApplicationConfig.ComponentConfig.KinematicModelConfig.builder()
                    .type(KinematicModel.Type.SPRING_MODEL)
                    .kinematicModelSpec("stiffness", 80.0)
                    .kinematicModelSpec("unstretched_length", 90.0)
                    .kinematicModelSpec("model_u", "circ")
                    .kinematicModelSpec("model_v", "wall")
                    .build())
            .shape(
                ApplicationConfig.ComponentConfig.ShapeConfig.builder()
                    .type(Shape.Type.ZIGZAG_LINE)
                    .shapeSpec("zigzag_count", 10)
                    .shapeSpec("width", 15.0)
                    .build())
            .appearance(appearanceOf("color", "BLUE", "line_width", 3.0))
            .build();

    ApplicationConfig.TimelineConfig timeline =
        ApplicationConfig.TimelineConfig.builder()
            .type(ApplicationConfig.TimelineConfig.Type.NUMERIC)
            .timelineSpec("time_step", 0.002)
            .build();

    ApplicationConfig.CanvasConfig canvas =
        ApplicationConfig.CanvasConfig.builder()
            .width(400)
            .height(400)
            .naturalScreenRatio(1.0)
            .build();

    ApplicationConfig.FieldConfig field =
        ApplicationConfig.FieldConfig.builder()
            .type(ApplicationConfig.FieldConfig.Type.GRAVITY)
            .fieldSpec("gx", 3.5)
            .build();

    ApplicationConfig expected =
        ApplicationConfig.builder()
            .name(name)
            .version(version)
            .refreshPeriod(refreshPeriod)
            .assembly(assemblyOf(massPoint, wall, spring))
            .fields(fieldsOf(field))
            .timeline(timeline)
            .canvas(canvas)
            .build();

    try (InputStream in =
        getClass().getResourceAsStream("/com/hzyi/jplab/core/application/single_circle.yaml")) {
      ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
      mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
      ApplicationConfig got = mapper.readValue(in, ApplicationConfig.class);
      assertThat(got.getName()).isEqualTo(expected.getName());
      assertThat(got.getTimeline()).isEqualTo(expected.getTimeline());
      assertThat(got.getCanvas()).isEqualTo(expected.getCanvas());
      assertThat(got.getAssembly().get(0)).isEqualTo(expected.getAssembly().get(0));
      assertThat(got.getAssembly().get(1).getKinematicModel())
          .isEqualTo(expected.getAssembly().get(1).getKinematicModel());
      assertThat(got.getAssembly().get(1).getShape())
          .isEqualTo(expected.getAssembly().get(1).getShape());
      assertThat(got.getAssembly().get(1).getAppearance())
          .isEqualTo(expected.getAssembly().get(1).getAppearance());
      assertThat(got.getAssembly().get(1)).isEqualTo(expected.getAssembly().get(1));
      assertThat(got.getAssembly().get(2)).isEqualTo(expected.getAssembly().get(2));
      assertThat(got).isEqualTo(expected);
    } catch (Exception e) {
      throw e;
    }
  }

  private static HashMap<String, Object> appearanceOf(String k, Object v, String k2, Object v2) {
    HashMap<String, Object> hashMap = new HashMap<>();
    hashMap.put(k, v);
    hashMap.put(k2, v2);
    return hashMap;
  }

  private static HashMap<String, Object> appearanceOf() {
    HashMap<String, Object> hashMap = new HashMap<>();
    return hashMap;
  }

  private static ArrayList<ApplicationConfig.ComponentConfig> assemblyOf(
      ApplicationConfig.ComponentConfig c,
      ApplicationConfig.ComponentConfig c2,
      ApplicationConfig.ComponentConfig c3) {
    return arrayListOf(c, c2, c3);
  }

  private static ArrayList<ApplicationConfig.FieldConfig> fieldsOf(
      ApplicationConfig.FieldConfig... fields) {
    return arrayListOf(fields);
  }

  private static <E> ArrayList<E> arrayListOf(E... elements) {
    ArrayList<E> arrayList = new ArrayList<>();
    for (E e : elements) {
      arrayList.add(e);
    }
    return arrayList;
  }
}
