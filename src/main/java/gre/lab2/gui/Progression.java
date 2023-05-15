package gre.lab2.gui;

/**
 * Etat du traitement des différents sommets lors de la création du labyrinthe.
 *
 * @see MazeBuilder
 */
public enum Progression {
  /**
   * Le sommet n'a pas encore été traité par l'algorithme.
   */
  PENDING,

  /**
   * Le traitement est partiellement effectué.
   */
  PROCESSING,

  /**
   * Le traitement est terminé.
   */
  PROCESSED
}
