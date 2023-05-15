package gre.lab2.gui.impl;

import gre.lab2.graph.GraphObserver;
import gre.lab2.graph.VertexLabelling;

public final class SolverMonitor implements VertexLabelling<Integer> {
  private final int[] labels;
  private final GraphObserver observer;

  public SolverMonitor(int size, GraphObserver observer) {
    this.labels = new int[size];
    this.observer = observer;
  }

  @Override
  public Integer getLabel(int v) {
    return labels[v];
  }

  @Override
  public void setLabel(int v, Integer label) {
    labels[v] = label;
    observer.onVertexChanged(v);
  }

  public int total() {
    int total = 0;
    for (int n : labels) {
      total += n;
    }
    return total;
  }
}
