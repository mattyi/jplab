package com.hzyi.jplab.core.model;

import com.hzyi.jplab.core.viewer.shape.ZigzagLine;
import com.hzyi.jplab.core.viewer.Painter;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.experimental.Accessors;
import com.hzyi.jplab.core.viewer.Appearance;

@Accessors(fluent = true)
@Builder
public class Spring extends ConnectingComponent implements ZigzagLine {

  @Getter private String name;
  @Getter private double stiffness;
  @Getter private Component componentA;
  @Getter private Component componentB; 
  @Getter private double connectingPointAX;
  @Getter private double connectingPointAY;
  @Getter private double connectingPointBX;
  @Getter private double connectingPointBY;
  @Getter @Setter private Assembly assembly;
  @Getter private double width;
  @Getter private double length;
  @Getter private Appearance appearance;

  public Painter getPainter() {
    return assembly.getPainterFactory().getZigzagLinePainter();
  }

  public ComponentState getInitialComponentState() {
    return null;
  }

  public String getName() {
    return name();
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