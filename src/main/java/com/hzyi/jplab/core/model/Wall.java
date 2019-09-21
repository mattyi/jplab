package com.hzyi.jplab.core.model;

import com.hzyi.jplab.core.viewer.shape.Edge;
import com.hzyi.jplab.core.viewer.Painter;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.experimental.Accessors;
import com.hzyi.jplab.core.viewer.Appearance;

@Builder(builderMethodName = "newBuilder")
@Accessors(fluent = true)
public class Wall extends StaticComponent implements Edge {

  @Getter private double x;
  @Getter private double y;
  @Getter private double theta;
  @Getter private String name;

  @Getter private double innerLineHeight;
  @Getter private double innerLineAngle;
  @Getter private double innerLineCount;
  @Getter private double length;

  @Getter private Appearance appearance;
  @Getter @Setter private Assembly assembly;

  public Painter getPainter() {
    return assembly.getPainterFactory().getEdgePainter();
  }

  public String getName() {
    return name();
  }

  public ComponentState getInitialComponentState() {
    return null;
  }

  public Appearance getAppearance() {
    return appearance();
  }

  public void update(ComponentState state) {
    return;
  }

  public void paint() {
    getPainter().paint(this, x(), y(), theta());
  }
}