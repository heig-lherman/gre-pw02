package gre.lab2.graph;

/**
 * Sujet capable de gérer une collection de {@link GraphObserver}, les notifiant lorsque certains événements
 * se produisent sur un graphe sous-jacent.
 *
 * @see Graph
 * @see GraphObserver
 */
public interface ObservableGraph {
  /**
   * <p>Enregistre un nouvel observateur.</p>
   *
   * <p>Un même observateur enregistré à de multiples reprises peut recevoir plusieurs notifications
   * pour un même événement.</p>
   *
   * <p>Enregister {@code null} n'a aucun effet.</p>
   *
   * @param observer Un {@link GraphObserver}.
   */
  void subscribe(GraphObserver observer);

  /**
   * <p>Interrompt l'envoi d'événéments à l'observateur donné, si préablement enregistré (sinon ne fait rien).</p>
   *
   * <p>Si l'observateur a été enregistré à de multiples reprises, le comportement est indéfini.</p>
   *
   * @param observer Un {@link GraphObserver}.
   */
  void unsubscribe(GraphObserver observer);
}
