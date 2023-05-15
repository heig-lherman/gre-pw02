package gre.lab2.gui.impl;

import gre.lab2.graph.*;
import gre.lab2.gui.MazeBuilder;
import gre.lab2.gui.Progression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public final class ObservableMaze
      implements GridGraph2D, VertexLabelling<Progression>, ObservableGraph, MazeBuilder {
  private final List<GraphObserver> subscribers = new ArrayList<>();
  private final Graph topology;
  private final GridGraph2D delegate;
  private final Progression[] vertexData;

  public ObservableMaze(Graph topology, GridGraph2D delegate) {
    this.topology = topology;
    this.delegate = delegate;
    this.vertexData = new Progression[delegate.nbVertices()];
    Arrays.fill(vertexData, Progression.PENDING);
  }

  @Override
  public synchronized List<Integer> neighbors(int v) {
    return delegate.neighbors(v);
  }

  @Override
  public synchronized List<Edge> edges() {
    return delegate.edges();
  }

  @Override
  public synchronized boolean areAdjacent(int u, int v) {
    return delegate.areAdjacent(u, v);
  }

  @Override
  public void addEdge(int u, int v) {
    synchronized (this) {
      delegate.addEdge(u, v);
    }
    notify(s -> s.onEdgeAdded(u, v));
  }

  @Override
  public void removeEdge(int u, int v) {
    synchronized (this) {
      delegate.removeEdge(u, v);
    }
    notify(s -> s.onEdgeRemoved(u, v));
  }

  @Override
  public synchronized int nbVertices() {
    return delegate.nbVertices();
  }

  @Override
  public synchronized boolean vertexExists(int v) {
    return delegate.vertexExists(v);
  }

  @Override
  public void setLabel(int v, Progression data) {
    assertVertexExists(v);

    if (data == vertexData[v])
      // Evite de notifier s'il n'y a pas de vrai changement.
      return;

    vertexData[v] = data;
    notify(s -> s.onVertexChanged(v));
  }

  @Override
  public Progression getLabel(int v) {
    assertVertexExists(v);
    return vertexData[v];
  }

  @Override
  public void subscribe(GraphObserver observer) {
    if (observer == null) return;
    subscribers.add(observer);
  }

  @Override
  public void unsubscribe(GraphObserver observer) {
    subscribers.remove(observer);
  }

  @Override
  public synchronized int width() {
    return delegate.width();
  }

  @Override
  public synchronized int height() {
    return delegate.height();
  }

  @Override
  public Graph topology() {
    return topology;
  }

  @Override
  public VertexLabelling<Progression> progressions() {
    return this;
  }

  @Override
  public void addWall(int u, int v) {
    if (areAdjacent(u, v))
      removeEdge(u, v);
  }

  @Override
  public void removeWall(int u, int v) {
    if (!topology.areAdjacent(u, v))
      throw new IllegalArgumentException("Can't remove the wall {" + u + ", " + v + "} " +
            "since it exists in the topology.");

    if (!areAdjacent(u, v))
      addEdge(u, v);
  }

  // Helpers

  private void notify(Consumer<GraphObserver> lambda) {
    for (var subscriber : subscribers) {
      lambda.accept(subscriber);
    }
  }

  private void assertVertexExists(int v) {
    if (!vertexExists(v))
      throw new IndexOutOfBoundsException("Vertex " + v + " out of bounds. Domain: [0," + width() * height() + "[");
  }
}
