package gre.lab2.gui;

import gre.lab2.graph.Graph;
import gre.lab2.graph.VertexLabelling;

/**
 * <p>Permet de construire un labyrinthe comme étant un graphe partiel d'un {@link Graph} connexe sous-jacent nommé
 * ici <i>topologie</i>. Ce graphe définit l'ensemble des murs et des passages entre les sommets. Une arête étant un
 * passage, l'absence d'arête un mur. S'il est possible de placer un mur où on le souhaite,
 * il est par contre impossible d'en supprimer un qui n'était pas un passage à l'origine.</p>
 */
public interface MazeBuilder {
  /**
   * <p>{@link Graph} connexe sous-jacent définissant les emplacements des murs et de passages. Une arête entre
   * deux sommets correspond à un passage.</p>
   *
   * <p>Ne change pas au cours de la construction.</p>
   *
   * @return Un {@link Graph}.
   */
  Graph topology();

  /**
   * <p>Indications supplémentaires sur la progression à fournir lors de la construction du labyrinthe.</p>
   *
   * <p>Par défaut, toutes les valeurs sont à {@link Progression#PENDING}.</p>
   *
   * @return Un {@link VertexLabelling}.
   */
  VertexLabelling<Progression> progressions();

  /**
   * Ajoute un nouveau mur au labyrinthe.
   *
   * @param u Une extrémité du mur.
   * @param v L'autre extrémité du mur.
   * @throws IndexOutOfBoundsException si <i>u</i> ou <i>v</i> n'existent pas dans le graphe.
   */
  void addWall(int u, int v);

  /**
   * Enlève un mur au labyrinthe.
   *
   * @param u Une extrémité du mur.
   * @param v L'autre extrémité du mur.
   * @throws IndexOutOfBoundsException si <i>u</i> ou <i>v</i> n'existent pas dans le graphe.
   * @throws IllegalArgumentException s'il n'y a pas d'arête reliant <i>u</i> à <i>v</v> dans la topologie.
   */
  void removeWall(int u, int v);
}
