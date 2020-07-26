package com.hzyi.jplab.core.timeline;

import com.hzyi.jplab.core.model.Assembly;

public interface Verifier {
  boolean verify(Assembly start, Assembly finish);

  Assembly onStart(Assembly snapshot);

  Assembly onFinish(Assembly snapshot);

  Verifier newVerifier();
}
