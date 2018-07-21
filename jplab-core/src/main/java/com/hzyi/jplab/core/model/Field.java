package com.hzyi.jplab.core.model;

import java.util.Map;
import java.util.HashMap;

public class Field {

  private static final Map<String, Field> fields = new HashMap<>();

  private final String name;

  public static final Field LOCX = add("loc_x");
  public static final Field LOCY = add("loc_y");
  public static final Field LOC = add("loc");
  public static final Field DIRX = add("dir_x");
  public static final Field DIRY = add("dir_y");
  public static final Field DIR = add("dir");
  public static final Field VX = add("v_x");
  public static final Field VY = add("v_y");
  public static final Field V = add("v");
  public static final Field OMEGA = add("omega");
  public static final Field AX = add("a_x");
  public static final Field AY = add("a_y");
  public static final Field ALPHA = add("alpha");
  public static final Field A = add("a");


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

  static Field add(String name) {
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