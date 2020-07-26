package com.hzyi.jplab.core.timeline;

import com.hzyi.jplab.core.model.Assembly;

/** An AlwaysPassVerifier's verification will always pass. */
public class AlwaysPassVerifier implements Verifier {

  private static final AlwaysPassVerifier instance = new AlwaysPassVerifier();

  public static Verifier instance() {
    return instance;
  }

  private AlwaysPassVerifier() {}

  public boolean verify(Assembly start, Assembly finish) {
    return true;
  }

  public Assembly onStart(Assembly snapshot) {
    throw new IllegalStateException("AlwaysPassVerifier: onStart should never be called.");
  }

  public Assembly onFinish(Assembly snapshot) {
    throw new IllegalStateException("AlwaysPassVerifier: onFinish should never be called.");
  }

  public Verifier newVerifier() {
    throw new IllegalStateException("AlwaysPassVerifier: newVerifier should never be called.");
  }
}
