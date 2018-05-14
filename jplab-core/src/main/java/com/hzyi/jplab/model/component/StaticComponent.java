package com.hzyi.jplab.model.component;

abstract class StaticComponent extends Component {

  public static class Builder<T extends Builder<T>>
      extends Component.Builder<T> {

    T setX(double x) {

    }

    T setY(double y) {

    }

    T setDirX(double dx) {

    }

    T setDirY(double dy) {

    }

    @Override


    public StaticComponent build() {

    }


  }

}