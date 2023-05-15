package gre.lab2.graph;

import java.util.List;

/**
 * <p>Graphe simple non orienté à nombre fixe de sommets <i>n</i>.</p>
 *
 * <p>Les sommets sont représentés par des nombres entiers entre 0 et <i>n - 1</i> (compris).</p>
 */
public interface Graph {
  /**
   * Retourne une liste modifiable (sans influence sur le graphe) des sommets adjacents au sommet donné en paramètre.
   *
   * @param v Un sommet
   * @return Les sommets adjacents de <i>v</i>.
   * @throws IndexOutOfBoundsException si <i>v</i> n'existe pas.
   */
  List<Integer> neighbors(int v);

  /**
   * <p>Liste modifiable (sans impacter le graphe) des arêtes du graphe.</p>
   *
   * <p>Il n'y a pas de doublons. L'arête <i>{u, v}</i> est matérialisée indifféremment par l <i>(u, v)</i>
   * ou le couple <i>(v, u)</i>.</p>
   *
   * @return Les arêtes du graphe.
   */
  List<Edge> edges();

  /**
   * Détermine si deux sommets donnés sont adjacents.
   *
   * @param u Un sommet.
   * @param v Un sommet.
   * @return {@code true} si <i>u</i> est adjacent à <i>v</i>, {@code false} sinon.
   * @throws IndexOutOfBoundsException si <i>u</i> ou <i>v</i> n'existent pas.
   */
  boolean areAdjacent(int u, int v);

  /**
   * @return Le nombre de sommets dans le graphe.
   */
  int nbVertices();

  /**
   * Vérifie l'existence d'un sommet donné.
   *
   * @param v Un sommet.
   * @return {@code true} si <i>v</i> est un sommet du graphe, sinon {@code false}.
   */
  boolean vertexExists(int v);


}
