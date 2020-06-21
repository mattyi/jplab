package com.hzyi.jplab.core.timeline;

import com.hzyi.jplab.core.model.AssemblySnapshot;

/** An AlwaysPassVerifier's verification will always pass. */
public class AlwaysPassVerifier implements Verifier {

  private static final AlwaysPassVerifier instance = new AlwaysPassVerifier();

  public static Verifier instance() {
    return instance;
  }

  private AlwaysPassVerifier() {}

  public boolean verify(AssemblySnapshot start, AssemblySnapshot finish) {
    return true;
  }

  public AssemblySnapshot onStart(AssemblySnapshot snapshot) {
    throw new IllegalStateException("AlwaysPassVerifier: onStart should never be called.");
  }

  public AssemblySnapshot onFinish(AssemblySnapshot snapshot) {
    throw new IllegalStateException("AlwaysPassVerifier: onFinish should never be called.");
  }

  public Verifier newVerifier() {
    throw new IllegalStateException("AlwaysPassVerifier: newVerifier should never be called.");
  }
}
