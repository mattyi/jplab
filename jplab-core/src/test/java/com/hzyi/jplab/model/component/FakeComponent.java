package com.hzyi.jplab.model.component;

/** A fake {@link Component} for testing */
class FakeComponent extends Component {

  FakeComponent(Component.Builder<?> builder) {
    super(builder);
  } 

  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder extends Component.Builder<Builder> {

    @Override
    public Component build() {
      return new FakeComponent(this);
    }
  }
}