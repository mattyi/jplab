package com.hzyi.jplab.core.model.kinematic;

import com.hzyi.jplab.core.timeline.Verifier;
import java.util.List;

/**
 * A VerifierProvider provides verifiers that verifies whether certain kinematic state of the
 * assembly is valid.
 */
public interface VerifierProvider {

  List<Verifier> verifiers();
}
