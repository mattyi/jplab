package com.hzyi.jplab.core.timeline;

import com.hzyi.jplab.core.model.AssemblySnapshot;

public interface Verifier {
  boolean verify(AssemblySnapshot start, AssemblySnapshot finish);

  AssemblySnapshot onStart(AssemblySnapshot snapshot);

  AssemblySnapshot onFinish(AssemblySnapshot snapshot);

  Verifier newVerifier();
}
