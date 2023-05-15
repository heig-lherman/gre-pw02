package gre.lab2.gui;

/**
 * Générateur de labyrinthes.
 *
 * @see MazeBuilder
 */
@FunctionalInterface
public interface MazeGenerator {
  /**
   * <p>Génère un nouveau labyrinthe grâce au builder donné.</p>
   *
   * <p>Tous les murs possibles doivent être pré-construits dans le builder lors de l'appel. Si tel n'est pas le cas,
   * le comportement est indéfini.</p>
   *
   * @param builder Un builder à qui déléguer les modifications de la structure de données.
   * @param from Sommet de départ, si l'algorithme utilisé en nécessite un.
   * @throws NullPointerException si {@code builder} est {@code null}.
   * @throws IllegalArgumentException si {@code from} n'est pas un sommet du graphe sous-jacent de {@code builder}.
   */
  void generate(MazeBuilder builder, int from);
}
