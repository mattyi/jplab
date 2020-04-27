package com.hzyi.jplab.core.util;

import com.hzyi.jplab.core.application.exceptions.IllegalPropertyTypeException;
import com.hzyi.jplab.core.application.exceptions.IllegalPropertyValueException;
import com.hzyi.jplab.core.application.exceptions.MissingRequiredPropertyException;
import java.util.Map;
import java.util.function.BiFunction;
import lombok.AllArgsConstructor;
import lombok.Getter;

/* A helper class that helps pop entries in a map into a builder object. */
@AllArgsConstructor
public class UnpackHelper<BuilderT> {

  @Getter private final BuilderT builder;
  private final Map<String, ?> source;
  private final Class<?> destinationType;

  /**
   * Used by `unpack`. Caller will call `test` on certain values and if it evaluates to false,
   * caller will call `getException` to create a Throwable and throw it.
   */
  public interface ThrowingPredicate<PropertyT, ThrowableT extends RuntimeException> {
    boolean test(PropertyT value);

    ThrowableT getException(
        String property, PropertyT value, Class<PropertyT> expectedType, Class<?> destinationType);
  }

  /** Creates an UnpackHelper. */
  public static <BuilderT> UnpackHelper<BuilderT> of(
      BuilderT builder, Map<String, ?> source, Class<?> destinationType) {
    return new UnpackHelper(builder, source, destinationType);
  }

  public <PropertyT> UnpackHelper<BuilderT> unpack(
      String property,
      Class<PropertyT> expectedType,
      BiFunction<BuilderT, PropertyT, BuilderT> collector) {
    return unpack(property, expectedType, collector, new ThrowingPredicate[0]);
  }

  public <PropertyT> UnpackHelper<BuilderT> unpackRequiredPositive(
      String property,
      Class<PropertyT> expectedType,
      BiFunction<BuilderT, PropertyT, BuilderT> collector) {
    return unpack(property, expectedType, collector, checkExistence(), checkPositivity());
  }

  public <PropertyT> UnpackHelper<BuilderT> unpackPositive(
      String property,
      Class<PropertyT> expectedType,
      BiFunction<BuilderT, PropertyT, BuilderT> collector) {
    return unpack(property, expectedType, collector, checkPositivity());
  }

  public <PropertyT> UnpackHelper<BuilderT> unpack(
      String property,
      Class<PropertyT> expectedType,
      BiFunction<BuilderT, PropertyT, BuilderT> collector,
      ThrowingPredicate<PropertyT, ?>... checkers) {

    Object value = source.get(property);
    if (value != null && !expectedType.isInstance(value)) {
      throw new IllegalPropertyTypeException(
          property, value.getClass().getName(), expectedType.getName());
    }

    PropertyT valueT = (PropertyT) value;
    for (ThrowingPredicate<PropertyT, ?> checker : checkers) {
      if (!checker.test(valueT)) {
        throw checker.getException(property, valueT, expectedType, destinationType);
      }
    }

    if (valueT != null) {
      collector.apply(builder, valueT);
    }
    return this;
  }

  /** Checks if input is non-null. Provides a MissingRequiredPropertyException otherwise. */
  public static <PropertyT>
      ThrowingPredicate<PropertyT, MissingRequiredPropertyException> checkExistence() {
    return new ThrowingPredicate<PropertyT, MissingRequiredPropertyException>() {
      @Override
      public boolean test(PropertyT value) {
        return value != null;
      }

      @Override
      public MissingRequiredPropertyException getException(
          String property,
          PropertyT value,
          Class<PropertyT> expectedType,
          Class<?> destinationType) {
        return new MissingRequiredPropertyException(destinationType.getName() + "." + property);
      }
    };
  }

  /**
   * Checks if input is a number and is greater than zero. Provides an IllegalPropertyValueException
   * otherwise.
   */
  public static <PropertyT>
      ThrowingPredicate<PropertyT, IllegalPropertyValueException> checkPositivity() {
    return new ThrowingPredicate<PropertyT, IllegalPropertyValueException>() {
      @Override
      public boolean test(PropertyT value) {
        return value == null
            || (value instanceof Integer && (Integer) value > 0)
            || (value instanceof Double && (Double) value > 0.0);
      }

      @Override
      public IllegalPropertyValueException getException(
          String property,
          PropertyT value,
          Class<PropertyT> expectedType,
          Class<?> destinationType) {
        if (!(value instanceof Number)) {
          return new IllegalPropertyValueException(
              destinationType.getName() + "." + property,
              String.format("not a number, type: %s", value.getClass()));
        }
        return new IllegalPropertyValueException(
            destinationType.getName() + "." + property, "must be positive");
      }
    };
  }
}
