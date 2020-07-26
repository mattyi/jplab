package com.hzyi.jplab.core.model.kinematic;

import com.google.common.collect.Table;
import com.hzyi.jplab.core.model.Constraint;
import com.hzyi.jplab.core.model.Property;

/**
 * A MultiplierProvider provides coefficients to unknowns or constants of the kinematic governing
 * equations of the assembly.
 */
public interface MultiplierProvider {

  Table<Constraint, Property, Double> codependentMultipliers(double timeStep);
}
