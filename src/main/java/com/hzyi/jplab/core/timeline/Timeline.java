package com.hzyi.jplab.core.timeline;

import org.threeten.bp.Instant;

public interface Timeline {

  Assembly getAssemblySnapshot(Instant timestamp);

}