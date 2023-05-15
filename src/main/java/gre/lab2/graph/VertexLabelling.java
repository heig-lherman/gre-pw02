package gre.lab2.graph;

/**
 * Interface d'un objet gérant l'étiquetage des sommets d'un {@link Graph} sous-jacent
 * avec des étiquettes d'un type {@link T} donné.
 *
 * @param <T> Type de l'étiquette.
 *
 * @see Graph
 */
public interface VertexLabelling<T> {
  /**
   * Récupère l'étiquette associée au sommet <i>v</i>.
   *
   * @param v Un sommet.
   * @return L'étiquette associée à <i>v</i>.
   * @throws IndexOutOfBoundsException si <i>v</i> n'est pas présent dans le graphe.
   */
  T getLabel(int v);

  /**
   * Assigne une étiquette au sommet <i>v</i>.
   *
   * @param v Un sommet.
   * @param label Etiquette à associer.
   * @throws IndexOutOfBoundsException si <i>v</i> n'est pas présent dans le graphe.
   * @throws IllegalArgumentException si {@code label} ne convient pas aux critères de l'implémentation.
   */
  void setLabel(int v, T label);
}
