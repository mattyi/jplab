package com.hzyi.jplab.core.application.exceptions;

import java.util.Map;

public class Prechecks {

  private Prechecks() {}

  public static void checkFieldValue(
      boolean valid, String entity, String field, String format, String... args) {
    if (!valid) {
      throw new IllegalFieldValueException(entity + "." + field, String.format(format, args));
    }
  }

  public static <T> T checkFieldExists(T value, String entity, String field) {
    if (value == null) {
      throw new MissingRequiredFieldException(entity + "." + field);
    }
    return value;
  }

  public static <T> T checkFieldExists(Map<String, ?> map, String entity, String field) {
    if (!map.containsKey(field)) {
      throw new MissingRequiredFieldException(entity + "." + field);
    }
    return (T) map.get(field);
  }
}
