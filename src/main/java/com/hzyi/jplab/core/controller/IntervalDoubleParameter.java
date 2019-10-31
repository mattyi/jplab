package com.hzyi.jplab.core.controller;

/**
 * A {@code Parameter} whose value can only be between min and max and can only be equal to min +
 * (max - min) / interval * i
 */
public class IntervalDoubleParameter extends ParameterImpl<Double> {

  private double min, max;
  private int numIntervals;

  public IntervalDoubleParameter(
      double initValue, String name, double min, double max, int numIntervals) {
    super(initValue, name);
    this.min = min;
    this.max = max;
    this.numIntervals = numIntervals;
    if (value < min || value > max) {
      throw new IllegalArgumentException("value " + value + " is not valid");
    }
    value = round(value, min, max, numIntervals);
  }

  /** value would be rounded to the closest possible value */
  @Override
  public void setValue(Double value) {
    if (value < min || value > max) {
      throw new IllegalArgumentException("value " + value + " is not valid");
    }
    value = round(value, min, max, numIntervals);
    super.setValue(value);
  }

  public double getMin() {
    return min;
  }

  public double getMax() {
    return max;
  }

  public double getInterval() {
    return (max - min) / numIntervals;
  }

  public int getNumInterVals() {
    return numIntervals;
  }

  private static double round(double value, double min, double max, double numIntervals) {
    double increm = (max - min) / (double) numIntervals;
    double tmp = (value - min) / increm;
    int interval = (int) (tmp + 0.5);
    return min + interval * increm;
  }
}
