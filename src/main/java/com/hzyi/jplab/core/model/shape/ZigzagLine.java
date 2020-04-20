package com.hzyi.jplab.core.model.shape;

import static com.hzyi.jplab.core.util.UnpackHelper.checkExistence;
import static com.hzyi.jplab.core.util.UnpackHelper.checkPositivity;

import com.hzyi.jplab.core.util.UnpackHelper;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Builder(builderMethodName = "newBuilder")
public class ZigzagLine implements Shape {

  @Getter
  @Accessors(fluent = true)
  private double width;

  @Getter
  @Accessors(fluent = true)
  private int zigzagCount = 10;

  @Getter @Deprecated private Appearance appearance;

  public final Shape.Type type() {
    return Shape.Type.ZIGZAG_LINE;
  }

  public static ZigzagLine unpack(Map<String, ?> map) {
    ZigzagLineBuilder builder = newBuilder();
    UnpackHelper<ZigzagLineBuilder> u = UnpackHelper.of(builder, map, ZigzagLine.class);
    u.unpack("width", Double.class, ZigzagLineBuilder::width, checkExistence(), checkPositivity());
    u.unpack("zigzag_count", Integer.class, ZigzagLineBuilder::zigzagCount, checkPositivity());
    return u.getBuilder().build();
  }
}
