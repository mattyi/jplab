package com.hzyi.jplab.core.application;

import static com.google.common.base.Preconditions.checkArgument;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.collect.ImmutableList;
import com.hzyi.jplab.core.application.config.ApplicationConfig;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class ApplicationMain {

  private static final String BASE_PATH = "/com/hzyi/jplab/application";

  // mvn exec:java classpath.yaml
  public static void main(String[] args) {
    checkArgument(args.length <= 1, "expecting one or no argument, got %s", args);
    if (args.length == 0) {
      runInteractive();
    } else {
      run(args[0]);
    }
  }

  private static void run(String configFile) {
    Application.reset();
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

  private static void runInteractive() {
    List<Path> options = getOptions();
    Scanner scanner = new Scanner(System.in);
    while (true) {
      printOptions(options);
      int num = scanner.nextInt();
      if (num < 0 || num >= options.size()) {
        System.out.println(
            "invalid number; please select a valid number between 0 and " + (options.size() - 1));
      } else {
        run(options.get(num).getFileName().toString());
      }
    }
  }

  private static List<Path> getOptions() {
    try {
      Path basePath = Paths.get(ApplicationMain.class.getResource(BASE_PATH).toURI());
      return Files.list(basePath).collect(ImmutableList.toImmutableList());
    } catch (URISyntaxException | IOException e) {
      System.out.println(e);
      System.exit(1);
    }
    return ImmutableList.of();
  }

  private static void printOptions(List<Path> options) {
    System.out.printf(
        "running in interactive mode, please select one of the following: 0 - %d:\n",
        options.size() - 1);
    System.out.println();
    for (int i = 0; i < options.size(); i++) {
      String fileName = options.get(i).toString();
      fileName = fileName.substring(fileName.lastIndexOf('/') + 1, fileName.lastIndexOf('.'));
      System.out.printf("%d. %s\n", i, fileName);
    }
  }
}
