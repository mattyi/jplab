package com.hzyi.jplab.core.application;

import com.google.auto.value.AutoValue;
import com.hzyi.jplab.core.application.ui.PrimaryStageFactory;
import com.hzyi.jplab.core.controller.Controller;
import com.hzyi.jplab.core.model.Assembly;
import com.hzyi.jplab.core.solver.Solver;
import com.hzyi.jplab.core.viewer.Displayer;

@AutoValue
public abstract class Application {

  public abstract String name();

  public abstract Assembly assembly();

  public abstract Solver solver();

  public abstract Controller controller();

  public abstract Displayer displayer();

  public static Builder newBuilder() {
    return new AutoValue_Application.Builder();
  }

  @AutoValue.Builder
  public static abstract class Builder {

    public abstract Builder name(String val);

    public abstract Builder assembly(Assembly val);

    public abstract Builder solver(Solver val);

    public abstract Builder displayer(Displayer val);

    public abstract Builder controller(Controller val);

    public abstract Application build();
  }
}
