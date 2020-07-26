package com.hzyi.jplab.core.model.kinematic;

import com.hzyi.jplab.core.model.Component;
import com.hzyi.jplab.core.model.Property;
import java.util.List;
import java.util.Map;

/**
 * A PropertyProvider provides unknown properties bound by the kinematic governing equations of the
 * assembly.
 */
public interface PropertyProvider {

  List<Property> properties();

  Component merge(Map<String, ?> map);

  Map<String, Object> pack();
}
