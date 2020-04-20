package com.hzyi.jplab.core.util;

import com.hzyi.jplab.core.application.exceptions.IllegalFieldTypeException;
import com.hzyi.jplab.core.application.exceptions.IllegalFieldValueException;
import com.hzyi.jplab.core.application.exceptions.MissingRequiredFieldException;
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
  public interface ThrowingPredicate<FieldT, ThrowableT extends RuntimeException> {
    boolean test(FieldT value);

    ThrowableT getException(
        String field, FieldT value, Class<FieldT> expectedType, Class<?> destinationType);
  }

  /** Creates an UnpackHelper. */
  public static <BuilderT> UnpackHelper<BuilderT> of(
      BuilderT builder, Map<String, ?> source, Class<?> destinationType) {
    return new UnpackHelper(builder, source, destinationType);
  }

  public <FieldT> UnpackHelper<BuilderT> unpack(
      String field, Class<FieldT> expectedType, BiFunction<BuilderT, FieldT, BuilderT> collector) {
    return unpack(field, expectedType, collector, new ThrowingPredicate[0]);
  }

  public <FieldT> UnpackHelper<BuilderT> unpackRequiredPositive(
      String field, Class<FieldT> expectedType, BiFunction<BuilderT, FieldT, BuilderT> collector) {
    return unpack(field, expectedType, collector, checkExistence(), checkPositivity());
  }

  public <FieldT> UnpackHelper<BuilderT> unpackPositive(
      String field, Class<FieldT> expectedType, BiFunction<BuilderT, FieldT, BuilderT> collector) {
    return unpack(field, expectedType, collector, checkPositivity());
  }

  public <FieldT> UnpackHelper<BuilderT> unpack(
      String field,
      Class<FieldT> expectedType,
      BiFunction<BuilderT, FieldT, BuilderT> collector,
      ThrowingPredicate<FieldT, ?>... checkers) {

    Object value = source.get(field);
    if (value != null && !expectedType.isInstance(value)) {
      throw new IllegalFieldTypeException(
          field, value.getClass().getName(), expectedType.getName());
    }

    FieldT valueT = (FieldT) value;
    for (ThrowingPredicate<FieldT, ?> checker : checkers) {
      if (!checker.test(valueT)) {
        throw checker.getException(field, valueT, expectedType, destinationType);
      }
    }

    if (valueT != null) {
      collector.apply(builder, valueT);
    }
    return this;
  }

  /** Checks if input is non-null. Provides a MissingRequiredFieldException otherwise. */
  public static <FieldT> ThrowingPredicate<FieldT, MissingRequiredFieldException> checkExistence() {
    return new ThrowingPredicate<FieldT, MissingRequiredFieldException>() {
      @Override
      public boolean test(FieldT value) {
        return value != null;
      }

      @Override
      public MissingRequiredFieldException getException(
          String field, FieldT value, Class<FieldT> expectedType, Class<?> destinationType) {
        return new MissingRequiredFieldException(destinationType.getName() + "." + field);
      }
    };
  }

  /**
   * Checks if input is a number and is greater than zero. Provides an IllegalFieldValueException
   * otherwise.
   */
  public static <FieldT> ThrowingPredicate<FieldT, IllegalFieldValueException> checkPositivity() {
    return new ThrowingPredicate<FieldT, IllegalFieldValueException>() {
      @Override
      public boolean test(FieldT value) {
        return value == null
            || (value instanceof Integer && (Integer) value > 0)
            || (value instanceof Double && (Double) value > 0.0);
      }

      @Override
      public IllegalFieldValueException getException(
          String field, FieldT value, Class<FieldT> expectedType, Class<?> destinationType) {
        if (!(value instanceof Number)) {
          return new IllegalFieldValueException(
              destinationType.getName() + "." + field,
              String.format("not a number, type: %s", value.getClass()));
        }
        return new IllegalFieldValueException(
            destinationType.getName() + "." + field, "must be positive");
      }
    };
  }
}
