package com.hzyi.jplab.core.model.kinematic;

import com.hzyi.jplab.core.util.Coordinate;
import com.hzyi.jplab.core.util.CoordinateSystem;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Builder(builderMethodName = "newBuilder", toBuilder = true)
public class SpringModel extends ConnectingModel {

  @Getter private String name;
  @Getter private double stiffness;
  @Getter private double originalLength;
  private double relativeConnectingPointAX;
  private double relativeConnectingPointAY;
  private double relativeConnectingPointBX;
  private double relativeConnectingPointBY;
  @Getter private KinematicModel connectingModelA;
  @Getter private KinematicModel connectingModelB;

  @Override
  public Coordinate relativeConnectingPointA() {
    return new Coordinate(relativeConnectingPointAX, relativeConnectingPointAY);
  }

  @Override
  public Coordinate relativeConnectingPointB() {
    return new Coordinate(relativeConnectingPointBX, relativeConnectingPointBY);
  }

  @Override
  public CoordinateSystem bodyCoordinateSystem() {
    throw new UnsupportedOperationException("not needed yet");
  }

  @Override
  public Coordinate getLocation() {
    throw new UnsupportedOperationException("not needed yet");
  }

  @Override
  public SpringModel unpack(Map<String, Object> map) {
    return toBuilder()
        .connectingModelA((KinematicModel) map.get("connecting_model_a"))
        .connectingModelB((KinematicModel) map.get("connecting_model_b"))
        .build();
  }
}
