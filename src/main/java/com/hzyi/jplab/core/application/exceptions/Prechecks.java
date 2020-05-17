package com.hzyi.jplab.core.application.exceptions;

import java.util.Map;

public class Prechecks {

  private Prechecks() {}

  public static void checkPropertyValue(
      boolean valid, String entity, String property, String format, String... args) {
    if (!valid) {
      throw new IllegalPropertyValueException(entity + "." + property, String.format(format, args));
    }
  }

  public static <T> T checkPropertyExists(T value, String entity, String property) {
    if (value == null) {
      throw new MissingRequiredPropertyException(entity + "." + property);
    }
    return value;
  }

  public static <T> T checkPropertyExists(Map<String, ?> map, String entity, String property) {
    if (!map.containsKey(property)) {
      throw new MissingRequiredPropertyException(entity + "." + property);
    }
    return (T) map.get(property);
  }

  public static void checkFeature(boolean valid, String format, String... args) {
    if (!valid) {
      throw new UnsupportedOperationException(String.format(format, args));
    }
  }
}
