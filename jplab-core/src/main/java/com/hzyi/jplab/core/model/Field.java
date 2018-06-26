package com.hzyi.jplab.core.model;

import java.util.Map;
import java.util.HashMap;

public class Field {

  private static final Map<String, Field> fields = new HashMap<>();

  private final String name;

  private Field(String name) {
    this.name = name;
  }

  static Field of(String name) {
    Field field = fields.get(name);
    if (field == null) {
      throw new IllegalArgumentException("Field with the name of " + name + " does not exist.");
    }
    return field;
  }

  static Field addField(String name) {
    if (fields.containsKey(name)) {
      throw new IllegalArgumentException("Field with the name of " + name + " already exists.");
    }
    Field field = new Field(name);
    fields.put(name, field);
    return field;
  }

  static void reset() {
    fields.clear();
  }
}