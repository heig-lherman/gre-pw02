package gre.lab2.graph;

/**
 * Sujet observant un graphe.
 *
 * @see Graph
 * @see ObservableGraph
 */
public interface GraphObserver {
  /**
   * <p>Evénement lancé à l'ajout d'un sommet.</p>
   *
   * <p>L'implémentation par défaut ne fait rien.</p>
   *
   * @param v Sommet ajouté.
   */
  default void onVertexAdded(int v) {}

  /**
   * <p>Evénement lancé au changement d'étiquette d'un sommet.</p>
   *
   * <p>L'implémentation par défaut ne fait rien.</p>
   *
   * @param v Sommet modifié.
   */
  default void onVertexChanged(int v) {}

  /**
   * <p>Evénement lancé à la suppression d'un sommet.</p>
   *
   * <p>L'implémentation par défaut ne fait rien.</p>
   *
   * @param v Sommet supprimé.
   */
  default void onVertexRemoved(int v) {}

  /**
   * <p>Evénement lancé à l'ajout d'une arête.</p>
   *
   * <p>L'implémentation par défaut ne fait rien.</p>
   *
   * @param u Une extrémité de l'arête ajoutée.
   * @param v L'autre extrémité de l'arête ajoutée.
   */
  default void onEdgeAdded(int u, int v) {}

  /**
   * <p>Evénement lancé au changement d'étiquette d'une arête.</p>
   *
   * <p>L'implémentation par défaut ne fait rien.</p>
   *
   * @param u Une extrémité de l'arête modifiée.
   * @param v L'autre extrémité de l'arête modifiée.
   */
  default void onEdgeChanged(int u, int v) {}

  /**
   * <p>Evénement lancé à la suppression d'une arête.</p>
   *
   * <p>L'implémentation par défaut ne fait rien.</p>
   *
   * @param u Une extrémité de l'arête supprimée.
   * @param v L'autre extrémité de l'arête supprimée.
   */
  default void onEdgeRemoved(int u, int v) {}
}
