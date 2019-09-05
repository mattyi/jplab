package com.hzyi.jplab.core.viewer.shape;

import java.util.List;

public interface CompoundShape extends Shape {

  List<Shape> getShapes();

}