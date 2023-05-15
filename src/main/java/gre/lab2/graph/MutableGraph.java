package gre.lab2.graph;

/**
 * Graphe simple non orienté mutable à nombre fixe de sommets.
 *
 * @see Graph
 */
public interface MutableGraph extends Graph {
  /**
   * Crée une nouvelle arête {<i>u</i> , <i>v</i>}.
   *
   * @param u Une extrémité de l'arête.
   * @param v L'autre extrémité de l'arête.
   * @throws IndexOutOfBoundsException si <i>u</i> ou <i>v</i> n'existent pas.
   * @throws IllegalArgumentException si l'arête existe ou s'il est impossible de créer une arête {<i>u</i> , <i>v</i>}.
   */
  void addEdge(int u, int v);

  /**
   * Supprime l'arête {<i>u</i> , <i>v</i>}.
   *
   * @param u Une extrémité de l'arête.
   * @param v L'autre extrémité de l'arête.
   * @throws IndexOutOfBoundsException si <i>u</i> ou <i>v</i> n'existent pas.
   * @throws IllegalArgumentException s'il est impossible de supprimer l'arête {<i>u</i> , <i>v</i>}.
   */
  void removeEdge(int u, int v);
}
