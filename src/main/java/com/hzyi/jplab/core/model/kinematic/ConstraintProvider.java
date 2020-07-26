package com.hzyi.jplab.core.model.kinematic;

import com.hzyi.jplab.core.model.Constraint;
import java.util.List;

/** A ContraintProvider provides kinematic contraints among one or more components. */
public interface ConstraintProvider {

  List<Constraint> constraints();
}
