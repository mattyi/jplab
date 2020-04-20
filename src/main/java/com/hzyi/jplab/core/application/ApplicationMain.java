package com.hzyi.jplab.core.application;

import static com.google.common.base.Preconditions.checkArgument;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.hzyi.jplab.core.application.config.ApplicationConfig;
import java.io.InputStream;

public class ApplicationMain {

  // mvn exec:java classpath.yaml
  public static void main(String[] args) {
    checkArgument(args.length > 0, "config path not given.");
    checkArgument(args.length <= 1, "expecting one argument, got %s", args);
    String configFile = args[0];

    try (InputStream in = ApplicationMain.class.getResourceAsStream(configFile)) {
      ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
      mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
      ApplicationConfig config = mapper.readValue(in, ApplicationConfig.class);
      Application.load(config);
      Application.run();
    } catch (Exception e) {
      System.out.println(e);
    }
  }
}
