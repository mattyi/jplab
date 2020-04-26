package com.hzyi.jplab.core.application;

import static com.google.common.base.Preconditions.checkArgument;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.hzyi.jplab.core.application.config.ApplicationConfig;
import java.io.InputStream;
import java.nio.file.Paths;

public class ApplicationMain {

  private static final String BASE_PATH = "/com/hzyi/jplab/application";

  // mvn exec:java classpath.yaml
  public static void main(String[] args) {
    checkArgument(args.length > 0, "config path not given.");
    checkArgument(args.length <= 1, "expecting one argument, got %s", args);
    String configFile = args[0];

    String configPath = Paths.get(BASE_PATH, configFile).toString();
    try (InputStream in = ApplicationMain.class.getResourceAsStream(configPath)) {
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
