package com.hzyi.jplab.core.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import com.hzyi.jplab.core.viewer.Painter;
import com.hzyi.jplab.core.viewer.DisplayContext;

@Builder(builderMethodName = "newBuilder")
public class StaticComponent implements Component {    

  private double initX;
  private double initY;
  private double initDirX;
  private double initDirY;
  @Getter private String name;
  @Getter private Painter painter;
  @Getter private DisplayContext displayContext;

  @Override 
  public ComponentState getInitialComponentState() {
    return new ComponentState(this)
        .put(Field.X, initX)
        .put(Field.Y, initY)
        .put(Field.DIRX, initDirX)
        .put(Field.DIRY, initDirY);
  }
}
