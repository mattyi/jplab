package com.hzyi.jplab.core.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.hzyi.jplab.core.application.config.ApplicationConfig;
import java.io.InputStream;
import org.junit.Test;

public class ApplicationFactoryTest {

  @Test
  public void testCreateApplication() throws Exception {

    try (InputStream in =
        getClass().getResourceAsStream("/com/hzyi/jplab/core/application/single_circle.yaml")) {
      ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
      mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
      ApplicationConfig config = mapper.readValue(in, ApplicationConfig.class);
      Application.load(config);
    } catch (Exception e) {
      throw e;
    }
  }
}
