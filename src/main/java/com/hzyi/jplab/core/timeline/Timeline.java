package com.hzyi.jplab.core.timeline;

import com.hzyi.jplab.core.model.AssemblySnapshot;

/** A timeline is used to report the kinematic state of the assembly at any given time. */
public interface Timeline {

  /** Returns the latest AssemblySnapshot. */
  AssemblySnapshot getLatestAssemblySnapshot();

  /** Moves the timeline forward for a period of time. */
  void advance(double timeStep);

  /** Moves the timeline forward for the period time returned by `getTimeStep()`. */
  void advance();

  /**
   * Returns the default time step used when advancing the timeline without a caller-provided time
   * step. This method should always return the same value. It's up to the implementation to choose
   * either a reasonable default value or make it configurable.
   */
  double getTimeStep();

  /** Returns the latest timestamp this timeline is at. */
  double getTimestamp();
}
