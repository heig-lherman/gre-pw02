package gre.lab2.groupe11;

import java.util.stream.IntStream;

/**
 * Implementation of a union-find data structure using <i>union by rank</i>
 * and <i>path halving</i> methods.
 * <p>
 * Vertices are represented by integers in the range {@code [0, size)}.
 * Initially, each vertex is in its own connected component at rank 0.
 */
final class UnionFind {

  private final int[] parents;
  private final int[] ranks;

  public UnionFind(int size) {
    parents = IntStream.range(0, size).toArray();
    ranks = new int[size];
  }

  /**
   * Finds the parent vertex using <i>path halving</i>.
   *
   * @param v The vertex for which to find the parent
   * @return The parent vertex of {@code v}
   * @throws IndexOutOfBoundsException if {@code v} is out of bounds
   */
  public int find(int v) {
    assertWithinBounds(v);
    // standard path halving methodology
    while (v != parents[v]) {
      parents[v] = parents[parents[v]];
      v = parents[v];
    }

    return v;
  }

  /**
   * Joins the connected components of two vertices using <i>union by rank</i>.
   *
   * @param u The first vertex
   * @param v The second vertex
   * @return {@code false} if the vertices are already in the same connected
   *         component,
   *         {@code true} otherwise
   * @throws IndexOutOfBoundsException if {@code u} or {@code v} is out of bounds
   */
  public boolean union(int u, int v) {
    // representatives (checks bounds implicitly)
    int x = find(u);
    int y = find(v);
    if (x == y) {
      // if equal, they are already in the same connected component
      return false;
    }

    // union by rank
    if (ranks[x] > ranks[y]) {
      parents[y] = x;
    } else {
      parents[x] = y;
      if (ranks[x] == ranks[y]) {
        ranks[y] = ranks[y] + 1;
      }
    }

    return true;
  }

  private void assertWithinBounds(int v) {
    if (v < 0 || v >= parents.length) {
      throw new IndexOutOfBoundsException("Vertex " + v + " is out of bounds");
    }
  }
}
