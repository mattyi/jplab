package com.hzyi.jplab.core.model;

/** A fake {@link Component} for testing */
class FakeComponent extends Component {

  FakeComponent(Builder builder) {
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