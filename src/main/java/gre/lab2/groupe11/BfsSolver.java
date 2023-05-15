package gre.lab2.groupe11;

import gre.lab2.graph.Graph;
import gre.lab2.graph.VertexLabelling;
import gre.lab2.gui.MazeSolver;

import java.util.*;

/**
 * Implementation of the BFS algorithm to solve any type of maze represented
 * by a {@link Graph} of any kind, as long as all the vertices are related
 * (otherwise might lead to unexpected results).
 */
public final class BfsSolver implements MazeSolver {

  /**
   * {@inheritDoc}
   *
   * @param graph       A {@link Graph} representing the maze
   * @param source      Start vertex
   * @param destination End vertex
   * @param treatments  Indication of the vertices treated by the algorithm and
   *                    the number of times they have been treated
   * @return An unmodifiable list representing the path from {@code source} to
   *         {@code destination}
   * @throws NullPointerException     if {@code graph} or {@code treatments} are
   *                                  {@code null}.
   * @throws IllegalArgumentException if {@code source} or {@code destination} are
   *                                  not within the {@code graph}.
   */
  @Override
  public List<Integer> solve(Graph graph, int source, int destination, VertexLabelling<Integer> treatments) {
    if (null == graph || null == treatments) {
      throw new NullPointerException("graph or treatments is null");
    }

    if (!graph.vertexExists(source) || !graph.vertexExists(destination)) {
      throw new IllegalArgumentException("source or destination is not a vertex of graph");
    }

    Queue<Integer> queue = new LinkedList<>();
    // initialise a visited vertex map so that we can keep track of the path
    List<Integer> visited = new ArrayList<>(Collections.nCopies(graph.nbVertices(), -1));

    // mark source as visited
    treatments.setLabel(source, 1);
    visited.set(source, 0);

    // BFS traversal starting from source until we discover the destination node
    int current = source;
    while (current != destination) {
      for (int v : graph.neighbors(current)) {
        // only go through unvisited vertices
        if (-1 == visited.get(v)) {
          // mark as visited and add to queue
          treatments.setLabel(v, 1);
          // we register the "current" vertex as the vertex that led us to this one
          visited.set(v, current);
          queue.add(v);
        }
      }

      current = queue.remove();
    }

    // Build the path from destination to source
    LinkedList<Integer> path = new LinkedList<>();
    path.addFirst(current);
    while (current != source) {
      // we use the visited vertices map we have to go towards the source
      current = visited.get(current);
      path.addFirst(current);
    }

    return Collections.unmodifiableList(path);
  }
}
