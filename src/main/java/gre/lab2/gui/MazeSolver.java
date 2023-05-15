package gre.lab2.gui;

import gre.lab2.graph.Graph;
import gre.lab2.graph.VertexLabelling;

import java.util.List;

/**
 * Solver de labyrinthes.
 */
@FunctionalInterface
public interface MazeSolver {
  /**
   * <p>Détermine un chemin (pas forcément optimal) entre deux positions dans un labyrinthe représenté par un graphe.</p>
   *
   * <p>Le chemin retourné est la liste ordonnée de tous les sommets parcourus depuis {@code source}
   * jusqu'à {@code destination} (tous deux inclus).</p>
   *
   * <p>A chaque traitement d'un sommet, le nombre de fois que ce sommet a été traité est mis à jour en incrémentant
   * l'étiquette correspondante dans {@code treatments}.</p>
   *
   * <p>Si le graphe fourni n'est pas connexe, le comportement est indéfini.</p>
   *
   * @param graph Un {@link Graph} représentant le labyrinthe.
   * @param source Sommet de départ.
   * @param destination Sommet de destination.
   * @param treatments Indication des sommets traités par l'algorithme et du nombre de fois qu'ils l'ont été.
   *
   * @return Une liste (modifiable ou non) représentant le chemin de {@code source} à {@code destination}.
   * @throws NullPointerException si {@code builder} ou {@code treatments} sont {@code null}.
   * @throws IllegalArgumentException si {@code source} ou  {@code destination} ne sont pas des sommets de {@code graph}.
   */
  List<Integer> solve(Graph graph, int source, int destination, VertexLabelling<Integer> treatments);
}
