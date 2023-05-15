package gre.lab2.groupe11;

import gre.lab2.graph.Edge;
import gre.lab2.gui.MazeBuilder;
import gre.lab2.gui.MazeGenerator;
import gre.lab2.gui.Progression;

import java.util.Collections;
import java.util.List;

/**
 * Implementation of the Kruskal algorithm to generate a maze.
 * <p>
 * This implementation uses the {@link UnionFind} data structure
 * to keep track of the connected components.
 */
public final class KruskalMazeGenerator implements MazeGenerator {

  /**
   * {@inheritDoc}
   *
   * @param builder A generic builder to which data structure modifications can be
   *                delegated.
   * @param from    (unused in this case, the Kruskal implementation will pick
   *                vertices at random)
   */
  @Override
  public void generate(MazeBuilder builder, int from) {
    List<Edge> edges = builder.topology().edges();
    // instead of sorting the edges, we shuffle them so that the generated maze is
    // random
    Collections.shuffle(edges);

    // we use a UnionFind data structure to keep track of the connected components
    UnionFind uf = new UnionFind(builder.topology().nbVertices());
    for (Edge e : edges) {
      // if the vertices are not in the same connected component, we merge them
      if (uf.union(e.u(), e.v())) {
        // mark the vertices as processed and remove the wall between them
        builder.progressions().setLabel(e.u(), Progression.PROCESSED);
        builder.progressions().setLabel(e.v(), Progression.PROCESSED);
        builder.removeWall(e.u(), e.v());
      }
    }
  }
}
