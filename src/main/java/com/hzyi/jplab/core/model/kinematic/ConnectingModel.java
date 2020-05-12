package com.hzyi.jplab.core.model.kinematic;

import com.hzyi.jplab.core.application.exceptions.Prechecks;
import com.hzyi.jplab.core.model.AssemblySnapshot;
import com.hzyi.jplab.core.painter.CoordinateTransformer;
import com.hzyi.jplab.core.util.Coordinate;
import com.hzyi.jplab.core.util.Coordinates;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public abstract class ConnectingModel implements KinematicModel {

  public abstract SingleKinematicModel connectingModelA();

  public abstract Coordinate relativeConnectingPointA();

  public Coordinate absoluteConnectingPointA() {
    return Coordinates.transform(
        relativeConnectingPointA(),
        connectingModelA().bodyCoordinateSystem(),
        CoordinateTransformer.absoluteNatural());
  }

  public abstract SingleKinematicModel connectingModelB();

  public abstract Coordinate relativeConnectingPointB();

  public Coordinate absoluteConnectingPointB() {
    return Coordinates.transform(
        relativeConnectingPointB(),
        connectingModelB().bodyCoordinateSystem(),
        CoordinateTransformer.absoluteNatural());
  }

  public double length() {
    return Coordinates.distance(absoluteConnectingPointA(), absoluteConnectingPointB());
  }

  public final double theta() {
    return Math.atan2(
        absoluteConnectingPointB().y() - absoluteConnectingPointA().y(),
        absoluteConnectingPointB().x() - absoluteConnectingPointA().x());
  }

  @Override
  public Map<String, Object> pack() {
    Map<String, Object> answer = new HashMap<>();
    answer.put("connecting_model_a", connectingModelA());
    answer.put("connecting_model_b", connectingModelB());
    return answer;
  }

  protected static interface ConnectingModelBuilder<B> {
    B connectingModelA(SingleKinematicModel model);

    B connectingModelB(SingleKinematicModel model);
  }

  protected static <B extends ConnectingModelBuilder<B>> BiFunction<B, String, B> newExtractor(
      Map<String, ?> map, final String entity, final String property) {
    final AssemblySnapshot snapshot = Prechecks.checkPropertyExists(map, "", "_assembly_snapshot");
    BiFunction<B, String, B> extractor =
        new BiFunction<B, String, B>() {
          @Override
          public B apply(B builder, String component) {
            KinematicModel model = snapshot.get(component);
            Prechecks.checkPropertyExists(model, entity, component);
            Prechecks.checkPropertyValue(
                model instanceof SingleKinematicModel,
                entity,
                component,
                "component %s is not a single kinematic model",
                component);

            if (property.equals("component_a")) {
              builder.connectingModelA((SingleKinematicModel) model);
            } else if (property.equals("component_b")) {
              builder.connectingModelB((SingleKinematicModel) model);
            } else {
              throw new IllegalStateException("internal: unknown property " + property);
            }
            return builder;
          }
        };
    return extractor;
  }
}
