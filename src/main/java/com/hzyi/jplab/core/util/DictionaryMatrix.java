package com.hzyi.jplab.core.util;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.hzyi.jplab.core.model.Constraint;
import com.hzyi.jplab.core.model.Property;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/** A real-valued matrix where each row and column is named. */
public class DictionaryMatrix {

  private final Map<Constraint, Integer> constraintIndices = new HashMap<>();
  private final Map<Property, Integer> propertyIndices = new HashMap<>();
  private final List<Constraint> constraints;
  private final List<Property> properties;
  private final RealMatrix matrix;

  public DictionaryMatrix(Collection<Constraint> constraints, Collection<Property> properties) {
    checkArgument(!constraints.isEmpty(), "empty constraints: expecting at least one");
    checkArgument(!properties.isEmpty(), "empty properties: expecting at least one");

    matrix = new Array2DRowRealMatrix(constraints.size(), properties.size());
    this.constraints = ImmutableList.copyOf(constraints);
    this.properties = ImmutableList.copyOf(properties);
    for (int i = 0; i < this.constraints.size(); i++) {
      Constraint c = this.constraints.get(i);
      Integer oldIndex = constraintIndices.put(c, i);
      checkArgument(oldIndex == null, "non-unique constraint: %s", c);
    }

    for (int i = 0; i < this.properties.size(); i++) {
      Property p = this.properties.get(i);
      Integer oldIndex = propertyIndices.put(p, i);
      checkArgument(oldIndex == null, "non-unique property: %s", p);
    }
  }

  public double get(Constraint con, Property prop) {
    return matrix.getEntry(getConstraintInd(con), getPropertyInd(prop));
  }

  public Map<Property, Double> getRow(Constraint constraint) {
    double[] matrixRow = matrix.getRow(getConstraintInd(constraint));
    ImmutableMap.Builder<Property, Double> builder = ImmutableMap.builder();
    for (int i = 0; i < properties.size(); i++) {
      builder.put(properties.get(i), matrixRow[i]);
    }
    return builder.build();
  }

  public void set(Constraint con, Property prop, double value) {
    matrix.setEntry(getConstraintInd(con), getPropertyInd(prop), value);
  }

  public void set(String con, String prop, double value) {
    set(Constraint.parse(con), Property.parse(prop), value);
  }

  public void add(Constraint con, Property prop, double value) {
    matrix.addToEntry(getConstraintInd(con), getPropertyInd(prop), value);
  }

  public void add(String con, String prop, double value) {
    add(Constraint.parse(con), Property.parse(prop), value);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    for (Property p : properties) {
      builder.append(p);
      builder.append(" ");
    }
    builder.append("\n");

    int i = 0;
    for (Constraint c : constraints) {
      builder.append(c);
      builder.append(" ");
      for (double value : matrix.getRow(i)) {
        builder.append(value);
        builder.append(" ");
      }
      builder.append("\n");
      i++;
    }

    return builder.toString();
  }

  public Map<Property, Double> getMapSolution() {
    RealVector x = solve();
    ImmutableMap.Builder<Property, Double> builder = ImmutableMap.builder();
    for (int i = 0; i < properties.size() - 1; i++) {
      builder.put(properties.get(i), x.getEntry(i));
    }
    return builder.build();
  }

  public Table<String, String, Double> getTableSolution() {
    RealVector x = solve();
    ImmutableTable.Builder<String, String, Double> builder = ImmutableTable.builder();
    for (int i = 0; i < properties.size() - 1; i++) {
      Property p = properties.get(i);
      builder.put(p.getModel(), p.getProperty(), x.getEntry(i));
    }
    return builder.build();
  }

  private RealVector solve() {
    int lastCol = properties.size() - 1;
    RealMatrix a = matrix.getSubMatrix(0, lastCol - 1, 0, lastCol - 1);
    RealVector b = matrix.getColumnVector(lastCol).mapMultiply(-1);
    RealVector x = new LUDecomposition(a).getSolver().solve(b);
    return x;
  }

  private int getPropertyInd(Property prop) {
    Integer i = propertyIndices.get(prop);
    checkArgument(i != null, "non-existing property: %s", prop);
    return i;
  }

  private int getConstraintInd(Constraint con) {
    Integer i = constraintIndices.get(con);
    checkArgument(i != null, "non-existing constraint: %s", con);
    return i;
  }
}
