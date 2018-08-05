package com.hzyi.jplab.core.application;

import com.hzyi.jplab.core.util.Buildable;

public interface Application extends Buildable {

  static class Assembly{}
  static class Solver{}
  static class Controller{}
  static class Visualizer{}

  String getApplicationName();

  Assembly getAssembly();

  Solver getSolver();

  Controller getController();

  void start();

}