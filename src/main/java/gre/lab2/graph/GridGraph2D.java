package gre.lab2.graph;

/**
 * <p>Graphe contraint à une grille rectangulaire. Ses sommets sont numérotés de 0 à
 * <i>{@link GridGraph2D#width()} * {@link GridGraph2D#height()} - 1</i>, ordonnés par ligne puis par colonne
 * (i.e. la première ligne contient les sommets 0 à <i>{@link GridGraph2D#width()} - 1</i>, et ainsi
 * de suite).</p>
 *
 * <p>Il n'est pas possible d'avoir une arête entre deux sommets non adjacents dans la grille.</p>
 */
public interface GridGraph2D extends MutableGraph {
  /**
   * @return Largeur de la grille.
   */
  int width();

  /**
   * @return Hauteur de la grille.
   */
  int height();
}
