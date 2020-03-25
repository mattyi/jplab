package com.hzyi.jplab.core.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
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
  private final String[] indices;
  private final RealMatrix matrix;

  public DictionaryMatrix(String... keys) {
    this(ImmutableList.copyOf(keys));
  }

  public DictionaryMatrix(Collection<String> keys) {
    Preconditions.checkArgument(
        !keys.isEmpty(), "empty keys: expecting CONST and at least one other key");
    Preconditions.checkArgument(
        keys.size() != 1,
        "only one key: expecting CONST and at least one other key, got %s",
        keys.iterator().next());

    matrix = new Array2DRowRealMatrix(keys.size() - 1, keys.size());
    indices = new String[keys.size()];
    int index = 0;
    boolean hasConst = false;
    for (String key : keys) {
      if (key.equals("CONST")) {
        hasConst = true;
        continue;
      }
      Integer oldIndex = indexNames.put(key, index);
      indices[index] = key;
      Preconditions.checkArgument(oldIndex == null, "non-unique key: %s", key);
      index++;
    }
    Preconditions.checkArgument(hasConst, "CONST not in keys");
    indexNames.put("CONST", index);
    indices[index] = "CONST";
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
    for (int i = 0; i < indexNames.size(); i++) {
      builder.put(indices[i], matrixRow[i]);
    }
    return builder.build();
  }

  public Map<String, Double> getCol(String col) {
    Integer c = indexNames.get(col);
    Preconditions.checkArgument(
        c != null, "non-existing key: %s, expecting one of: %s", col, indexNames.keySet());
    double[] matrixCol = matrix.getColumn(c);
    ImmutableMap.Builder<String, Double> builder = ImmutableMap.builder();
    for (int i = 0; i < indexNames.size() - 1; i++) {
      builder.put(indices[i], matrixCol[i]);
    }
    return builder.build();
  }

  public void set(String row, String col, double value) {
    Preconditions.checkArgument(!row.equals("CONST"), "non-existing row: CONST");
    Preconditions.checkArgument(indexNames.containsKey(row), "non-existing row: %s", row);
    Preconditions.checkArgument(indexNames.containsKey(col), "non-existing col: %s", col);
    matrix.setEntry(indexNames.get(row), indexNames.get(col), value);
  }

  public void add(String row, String col, double value) {
    Preconditions.checkArgument(!row.equals("CONST"), "non-existing row: CONST");
    Preconditions.checkArgument(indexNames.containsKey(row), "non-existing row: %s", row);
    Preconditions.checkArgument(indexNames.containsKey(col), "non-existing col: %s", col);
    matrix.addToEntry(indexNames.get(row), indexNames.get(col), value);
  }

  public Map<String, Double> solve() {
    int endRow = indexNames.size() - 2;
    RealMatrix a = matrix.getSubMatrix(0, endRow, 0, endRow);
    RealVector b = matrix.getColumnVector(endRow + 1).mapMultiply(-1);
    RealVector x = new LUDecomposition(a).getSolver().solve(b);
    ImmutableMap.Builder<String, Double> builder = ImmutableMap.builder();
    for (int i = 0; i <= endRow; i++) {
      builder.put(indices[i], x.getEntry(i));
    }
    return builder.build();
  }
}
