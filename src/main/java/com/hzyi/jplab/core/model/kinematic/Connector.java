package com.hzyi.jplab.core.model.kinematic;

import com.hzyi.jplab.core.application.Application;
import com.hzyi.jplab.core.application.exceptions.Prechecks;
import com.hzyi.jplab.core.model.AssemblySnapshot;
import com.hzyi.jplab.core.util.Coordinate;
import com.hzyi.jplab.core.util.CoordinateSystem;
import com.hzyi.jplab.core.util.Coordinates;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/** A connector is a kinematic model that connects two single kinematic models. */
public abstract class Connector implements KinematicModel {

  /** The first connected model. */
  public abstract SingleKinematicModel modelU();

  /** The point where modelU is connected to this connector, in modelU's body coordinate system. */
  public abstract Coordinate relativePointU();

  /** The second connected model. */
  public abstract SingleKinematicModel modelV();

  /** The point where modelV is connected to this connector, in modelV's body coordinate system. */
  public abstract Coordinate relativePointV();

  /** The point where modelU is connected to this connector, in natural coordinate system. */
  public Coordinate pointU() {

    Coordinate pointU =
        Coordinates.transform(
            relativePointU(),
            modelU().bodyCoordinateSystem(),
            Application.getPainterFactory().getCoordinateTransformer().natural());
    return pointU;
  }

  /** The point where modelV is connected to this connector, in natural coordinate system. */
  public Coordinate pointV() {
    Coordinate pointV =
        Coordinates.transform(
            relativePointV(),
            modelV().bodyCoordinateSystem(),
            Application.getPainterFactory().getCoordinateTransformer().natural());
    return pointV;
  }

  /**
   * The length of the connector. By default it's the distance between the two connecting points,
   * but may be overriden to refer to different concepts as appropriate in concrete classes.
   */
  public double length() {
    return Coordinates.distance(pointU(), pointV());
  }

  /** The angle between the vector from pointU to pointV and x-axis in natural system. */
  public double theta() {
    return Math.atan2(pointV().y() - pointU().y(), pointV().x() - pointU().x());
  }

  /**
   * The force that the connect has on components connected to it. Push forces are negative and pull
   * forces are positive.
   */
  public abstract double force();

  @Override
  public CoordinateSystem bodyCoordinateSystem() {
    throw new UnsupportedOperationException("not needed yet");
  }

  @Override
  public Map<String, Object> pack() {
    Map<String, Object> answer = new HashMap<>();
    answer.put("model_u", modelU());
    answer.put("model_v", modelV());
    return answer;
  }

  /**
   * A helper interface for unpacking connected models for subclasses of Connector. Builder classes
   * of a concrete connector has to implement this class in order to use connectedModelExtractor.
   */
  protected static interface ConnectorBuilder<B> {
    B modelU(SingleKinematicModel model);

    B modelV(SingleKinematicModel model);
  }

  /**
   * Creates a helper function used by an unpacker to unpack connected model from a map into the
   * builder of a concrete connector. The function created first looks up the name of the connected
   * model keyed by `model_u` or `model_v`, and then looks up the connected model in the assembly
   * snapshot by the name. This assumes the map contains a `_assembly_snapshot` and either `model_u`
   * or `model_v`, otherwise a MissingRequiredPropertyException will be thrown at executing the
   * function. The builder class must implement ConnectorBuilder too.
   */
  protected static <B extends ConnectorBuilder<B>> BiFunction<B, String, B> connectedModelExtractor(
      Map<String, ?> map, final String entity, final String property) {
    BiFunction<B, String, B> extractor =
        new BiFunction<B, String, B>() {
          @Override
          public B apply(B builder, String propertyValue) {
            AssemblySnapshot snapshot =
                Prechecks.checkPropertyExists(map, entity, "_assembly_snapshot");
            KinematicModel model = snapshot.getKinematicModel(propertyValue);
            Prechecks.checkPropertyExists(model, entity, property);
            Prechecks.checkPropertyValue(
                model instanceof SingleKinematicModel,
                entity,
                property,
                "component is not a single kinematic model");
            if (property.equals("model_u")) {
              builder.modelU((SingleKinematicModel) model);
            } else if (property.equals("model_v")) {
              builder.modelV((SingleKinematicModel) model);
            } else {
              throw new IllegalStateException("internal: unknown property " + property);
            }
            return builder;
          }
        };
    return extractor;
  }
}
