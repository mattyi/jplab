package com.hzyi.jplab.core.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
abstract class StaticComponent implements Component {
     
  private double initX;
  private double initY;
  private double initDirX;
  private double initDirY;
  @Getter private String name;
  @Getter private Painter painter;
  @Getter private DisplayContext displayContext;

  @Override 
  public ComponentState getInitComponentState() {
    return new ComponentState(this).put(Field.X, initX, Field.Y, initY, Field.DIRX, initDirX, Field.DIRY, initDirY);
  }
}