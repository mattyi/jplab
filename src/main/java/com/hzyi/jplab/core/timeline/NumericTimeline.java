package com.hzyi.jplab.core.timeline;

import com.hzyi.jplab.core.model.AssemblySnapshot;
import com.hzyi.jplab.core.model.kinematic.ConnectingModel;
import com.hzyi.jplab.core.model.kinematic.KinematicModel;
import com.hzyi.jplab.core.model.kinematic.SingleKinematicModel;
import com.hzyi.jplab.core.util.DictionaryMatrix;
import java.util.Map;
import lombok.Getter;

public class NumericTimeline implements Timeline {

  private final AssemblySnapshot initialAssemblySnapshot;
  @Getter private final double timeStep;
  private double timestamp;
  private AssemblySnapshot latestAssemblySnapshot;

  public NumericTimeline(AssemblySnapshot initialAssemblySnapshot) {
    this.initialAssemblySnapshot = initialAssemblySnapshot;
    this.latestAssemblySnapshot = initialAssemblySnapshot;
    this.timeStep = 0.02;
  }

  public NumericTimeline(AssemblySnapshot initialAssemblySnapshot, double timeStep) {
    this.initialAssemblySnapshot = initialAssemblySnapshot;
    this.latestAssemblySnapshot = initialAssemblySnapshot;
    this.timeStep = timeStep;
  }

  @Override
  public void advance(double timeStep) {
    AssemblySnapshot snapshot = advanceTimeStep(latestAssemblySnapshot, timeStep);
    latestAssemblySnapshot = adjustInternalState(snapshot);
    timestamp += timeStep;
  }

  @Override
  public AssemblySnapshot getLatestAssemblySnapshot() {
    return latestAssemblySnapshot;
  }

  @Override
  public double getTimestamp() {
    return timestamp;
  }

  @Override
  public void advance() {
    advance(this.timeStep);
  }

  static AssemblySnapshot advanceTimeStep(AssemblySnapshot snapshot, double timeStep) {
    AssemblySnapshot.Builder builder = AssemblySnapshot.newBuilder();
    for (Map.Entry<String, KinematicModel> entry : snapshot.getKinematicModels().entrySet()) {
      KinematicModel model = entry.getValue();
      if (builder.get(entry.getKey()) != null) {
        continue;
      }
      if (model instanceof ConnectingModel) {
        ConnectingModel connectingModel = (ConnectingModel) model;
        advanceTimeStep(builder, snapshot, connectingModel, timeStep);
      } else if (model instanceof SingleKinematicModel) {
        SingleKinematicModel singleModel = (SingleKinematicModel) model;
        advanceTimeStep(builder, singleModel, timeStep);
      } else {
        throw new IllegalStateException("unknown model type: " + model.getClass());
      }
    }
    return builder.build();
  }

  static AssemblySnapshot adjustInternalState(AssemblySnapshot snapshot) {
    DictionaryMatrix matrix = snapshot.toMatrix();
    Map<String, Double> answer = matrix.solve();
    snapshot = snapshot.unpack(answer);
    return snapshot;
  }

  private static SingleKinematicModel advanceTimeStep(
      AssemblySnapshot.Builder builder, SingleKinematicModel model, double timeStep) {
    if (builder.get(model.name()) != null) {
      return (SingleKinematicModel) builder.get(model.name());
    }
    Map<String, Object> map = model.pack();
    map.put("x", getDoubleValue(map, "x") + getDoubleValue(map, "vx") * timeStep);
    map.put("y", getDoubleValue(map, "y") + getDoubleValue(map, "vy") * timeStep);
    map.put("vx", getDoubleValue(map, "vx") + getDoubleValue(map, "ax") * timeStep);
    map.put("vy", getDoubleValue(map, "vy") + getDoubleValue(map, "ay") * timeStep);
    map.put("theta", getDoubleValue(map, "theta") + getDoubleValue(map, "omega") * timeStep);
    map.put("omega", getDoubleValue(map, "omega") + getDoubleValue(map, "alpha") * timeStep);
    SingleKinematicModel updatedModel = model.merge(map);
    builder.kinematicModel(updatedModel.name(), updatedModel);
    return updatedModel;
  }

  private static void advanceTimeStep(
      AssemblySnapshot.Builder builder,
      AssemblySnapshot snapshot,
      ConnectingModel model,
      double timeStep) {
    String connectingNameA = model.connectingModelA().name();
    String connectingNameB = model.connectingModelB().name();
    SingleKinematicModel connectingModelA = (SingleKinematicModel) builder.get(connectingNameA);
    SingleKinematicModel connectingModelB = (SingleKinematicModel) builder.get(connectingNameB);
    if (connectingModelA == null) {
      connectingModelA =
          advanceTimeStep(builder, (SingleKinematicModel) snapshot.get(connectingNameA), timeStep);
      builder.kinematicModel(connectingNameA, connectingModelA);
    }
    if (connectingModelB == null) {
      connectingModelB =
          advanceTimeStep(builder, (SingleKinematicModel) snapshot.get(connectingNameB), timeStep);
      builder.kinematicModel(connectingNameB, connectingModelB);
    }
    Map<String, Object> map = model.pack();
    map.put("connecting_model_a", connectingModelA);
    map.put("connecting_model_b", connectingModelB);
    builder.kinematicModel(model.name(), model.merge(map));
  }

  private static double getDoubleValue(Map<String, Object> map, String key) {
    return (Double) map.get(key);
  }
}
