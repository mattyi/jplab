package com.hzyi.jplab.core.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.hzyi.jplab.core.model.Property;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/** A real-valued matrix where each row and column is named. */
public class DictionaryMatrix {

  private final Map<String, Integer> indexNames = new HashMap<>();
  private final String[] rowKeys;
  private final String[] colKeys;
  private final RealMatrix matrix;

  public DictionaryMatrix(String... keys) {
    this(ImmutableList.copyOf(keys));
  }

  // public DictionaryMatrix(Collection<String> rowKeys, Collection<String> colKeys) {
  //   Preconditions.checkArgument(!rowKeys.isEmpty(), "empty keys: expecting at least one key");
  //   Preconditions.checkArgument(!colKeys.isEmpty(), "empty keys: expecting at least one key");
  //   matrix = new Array2DRowRealMatrix(rowKeys.size(), colKeys.size());
  //   for (String key : rowKeys) {
  //     Integer oldIndex = indexNames.put(key, index);
  //     rowKeys[index] = key;
  //     colKeys[index] = key;
  //     Preconditions.checkArgument(oldIndex == null, "non-unique key: %s", key);
  //     index++;
  //   }
  //   indexNames.put(Property.constant(), index);
  //   colKeys[index] = Property.constant();
  // }

  public DictionaryMatrix(Collection<String> keys) {
    Preconditions.checkArgument(!keys.isEmpty(), "empty keys: expecting at least one key");

    matrix = new Array2DRowRealMatrix(keys.size(), keys.size() + 1);
    rowKeys = new String[keys.size()];
    colKeys = new String[keys.size() + 1];
    int index = 0;
    for (String key : keys) {
      Integer oldIndex = indexNames.put(key, index);
      rowKeys[index] = key;
      colKeys[index] = key;
      Preconditions.checkArgument(oldIndex == null, "non-unique key: %s", key);
      index++;
    }
    indexNames.put(Property.constant(), index);
    colKeys[index] = Property.constant();
  }

  public double get(String row, String col) {
    Preconditions.checkArgument(indexNames.containsKey(row), "non-existing key: %s", row);
    Preconditions.checkArgument(indexNames.containsKey(col), "non-existing key: %s", col);
    return matrix.getEntry(indexNames.get(row), indexNames.get(col));
  }

  public Map<String, Double> getRow(String row) {
    Integer r = indexNames.get(row);
    Preconditions.checkArgument(
        r != null, "non-existing key: %s, expecting one of: %s", row, indexNames.keySet());
    double[] matrixRow = matrix.getRow(r);
    ImmutableMap.Builder<String, Double> builder = ImmutableMap.builder();
    for (int i = 0; i < colKeys.length; i++) {
      builder.put(colKeys[i], matrixRow[i]);
    }
    return builder.build();
  }

  public Map<String, Double> getCol(String col) {
    Integer c = indexNames.get(col);
    Preconditions.checkArgument(
        c != null, "non-existing key: %s, expecting one of: %s", col, indexNames.keySet());
    double[] matrixCol = matrix.getColumn(c);
    ImmutableMap.Builder<String, Double> builder = ImmutableMap.builder();
    for (int i = 0; i < rowKeys.length; i++) {
      builder.put(rowKeys[i], matrixCol[i]);
    }
    return builder.build();
  }

  public void set(String row, String col, double value) {
    Preconditions.checkArgument(!row.equals(Property.constant()), "non-existing row: %s", row);
    Preconditions.checkArgument(indexNames.containsKey(row), "non-existing row: %s", row);
    Preconditions.checkArgument(indexNames.containsKey(col), "non-existing col: %s", col);
    matrix.setEntry(indexNames.get(row), indexNames.get(col), value);
  }

  public void add(String row, String col, double value) {
    Preconditions.checkArgument(!row.equals(Property.constant()), "non-existing row: %s", row);
    Preconditions.checkArgument(indexNames.containsKey(row), "non-existing row: %s", row);
    Preconditions.checkArgument(indexNames.containsKey(col), "non-existing col: %s", col);
    matrix.addToEntry(indexNames.get(row), indexNames.get(col), value);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    for (String key : colKeys) {
      builder.append(key);
      builder.append(" ");
    }
    builder.append("\n");

    int i = 0;
    for (String key : rowKeys) {
      builder.append(key);
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

  public Map<String, Double> solve() {
    int lastCol = colKeys.length - 1;
    RealMatrix a = matrix.getSubMatrix(0, lastCol - 1, 0, lastCol - 1);
    RealVector b = matrix.getColumnVector(lastCol).mapMultiply(-1);
    RealVector x = new LUDecomposition(a).getSolver().solve(b);
    ImmutableMap.Builder<String, Double> builder = ImmutableMap.builder();
    for (int i = 0; i < lastCol; i++) {
      builder.put(colKeys[i], x.getEntry(i));
    }
    return builder.build();
  }

  public Map<String, Double> getMapSolution() {
    return solve();
  }

  public Table<String, String, Double> getTableSolution() {
    // System.out.println(this);
    int lastCol = colKeys.length - 1;
    RealMatrix a = matrix.getSubMatrix(0, lastCol - 1, 0, lastCol - 1);
    RealVector b = matrix.getColumnVector(lastCol).mapMultiply(-1);
    RealVector x = new LUDecomposition(a).getSolver().solve(b);
    ImmutableTable.Builder<String, String, Double> builder = ImmutableTable.builder();
    for (int i = 0; i < lastCol; i++) {
      Property property = Property.parse(colKeys[i]);
      builder.put(property.getModel(), property.getProperty(), x.getEntry(i));
    }
    return builder.build();
  }
}
