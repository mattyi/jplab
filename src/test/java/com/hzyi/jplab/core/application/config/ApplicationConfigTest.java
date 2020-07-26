package com.hzyi.jplab.core.application.config;

import static com.google.common.truth.Truth.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.hzyi.jplab.core.model.kinematic.Connector;
import com.hzyi.jplab.core.model.kinematic.SingleKinematicModel;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.Test;

public class ApplicationConfigTest {

  @Test
  public void testLoadConfig() throws IOException {
    String name = "Test Application";
    String version = "1";
    double refreshPeriod = 0.02;
    ApplicationConfig.KinematicModelConfig massPoint =
        ApplicationConfig.KinematicModelConfig.builder()
            .name("circ")
            .type(SingleKinematicModel.Type.MASS_POINT)
            .kinematicModelSpec("x", 20.0)
            .kinematicModelSpec("y", 0.0)
            .kinematicModelSpec("vx", 0.0)
            .kinematicModelSpec("vy", 0.0)
            .kinematicModelSpec("mass", 10.0)
            .kinematicModelSpec("radius", 20.0)
            .appearance(appearanceOf())
            .build();
    ApplicationConfig.KinematicModelConfig wall =
        ApplicationConfig.KinematicModelConfig.builder()
            .name("wall")
            .type(SingleKinematicModel.Type.STATIC_MODEL)
            .kinematicModelSpec("x", 20.0)
            .kinematicModelSpec("y", 100.0)
            .kinematicModelSpec("inner_line_count", 4)
            .kinematicModelSpec("inner_line_height", 10.0)
            .kinematicModelSpec("length", 40.0)
            .appearance(appearanceOf("color", "RED", "line_width", 1.0))
            .build();

    ApplicationConfig.ConnectorConfig spring =
        ApplicationConfig.ConnectorConfig.builder()
            .name("spring")
            .type(Connector.Type.SPRING_MODEL)
            .connectorSpec("stiffness", 80.0)
            .connectorSpec("unstretched_length", 90.0)
            .connectorSpec("model_u", "circ")
            .connectorSpec("model_v", "wall")
            .connectorSpec("zigzag_count", 10)
            .connectorSpec("width", 15.0)
            .appearance(appearanceOf("color", "BLUE", "line_width", 3.0))
            .build();

    ApplicationConfig.FieldConfig field =
        ApplicationConfig.FieldConfig.builder()
            .name("gravity_field")
            .type(ApplicationConfig.FieldConfig.Type.GRAVITY)
            .fieldSpec("gx", 3.5)
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

    ApplicationConfig expected =
        ApplicationConfig.builder()
            .name(name)
            .version(version)
            .refreshPeriod(refreshPeriod)
            .kinematicModels(kinematicModelsOf(massPoint, wall))
            .connectors(connectorsOf(spring))
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
      assertThat(got.getKinematicModels()).containsExactlyElementsIn(expected.getKinematicModels());
      assertThat(got.getConnectors()).containsExactlyElementsIn(expected.getConnectors());
      assertThat(got.getFields()).containsExactlyElementsIn(expected.getFields());
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

  private static ArrayList<ApplicationConfig.KinematicModelConfig> kinematicModelsOf(
      ApplicationConfig.KinematicModelConfig... models) {
    return arrayListOf(models);
  }

  private static ArrayList<ApplicationConfig.FieldConfig> fieldsOf(
      ApplicationConfig.FieldConfig... fields) {
    return arrayListOf(fields);
  }

  private static ArrayList<ApplicationConfig.ConnectorConfig> connectorsOf(
      ApplicationConfig.ConnectorConfig... connectors) {
    return arrayListOf(connectors);
  }

  private static <E> ArrayList<E> arrayListOf(E... elements) {
    ArrayList<E> arrayList = new ArrayList<>();
    for (E e : elements) {
      arrayList.add(e);
    }
    return arrayList;
  }
}
