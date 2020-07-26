package com.hzyi.jplab.core.model.shape;

import com.hzyi.jplab.core.util.UnpackHelper;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

@ToString
@Builder(builderMethodName = "newBuilder")
public class Edge implements Shape {

  @Getter
  @Accessors(fluent = true)
  private final double length;

  @Getter
  @Accessors(fluent = true)
  @Builder.Default
  private double innerLineAngle = Math.PI / 4;

  @Getter
  @Accessors(fluent = true)
  @Builder.Default
  private int innerLineCount = 4;

  @Getter
  @Accessors(fluent = true)
  @Builder.Default
  private double innerLineHeight = 10.0;

  public final Shape.Type type() {
    return Shape.Type.EDGE;
  }

  public static final Edge of(Map<String, ?> map) {
    EdgeBuilder builder = newBuilder();
    UnpackHelper<EdgeBuilder> helper = UnpackHelper.of(builder, map, Edge.class);
    return helper
        .unpack(
            "length",
            Double.class,
            EdgeBuilder::length,
            UnpackHelper.checkExistence(),
            UnpackHelper.checkPositivity())
        .unpack(
            "inner_line_angle",
            Double.class,
            EdgeBuilder::innerLineAngle,
            UnpackHelper.checkPositivity())
        .unpack(
            "inner_line_count",
            Integer.class,
            EdgeBuilder::innerLineCount,
            UnpackHelper.checkPositivity())
        .unpack(
            "inner_line_height",
            Double.class,
            EdgeBuilder::innerLineHeight,
            UnpackHelper.checkPositivity())
        .getBuilder()
        .build();
  }
}
