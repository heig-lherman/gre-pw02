package gre.lab2.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation de {@link GridGraph2D} pour laquelle toutes les opérations sont exécutées en temps constant de
 * la taille de la grille, sauf la récupération des arêtes exécutée temps linéaire. La complexité spatiale est linéaire
 * de la taille de la grile.
 */
public final class GridGraph implements GridGraph2D {
  /**
   * Direction dans laquelle peut se trouver un voisin.
   */
  private enum Direction {
    UP { public int offset(int width) { return -width; } },
    LEFT { public int offset(int width) { return -1; } },
    RIGHT { public int offset(int width) { return 1; } },
    DOWN { public int offset(int width) { return width; } };

    /**
     * Décalage entre deux numéros de sommets voisins selon la direction courante.
     *
     * @param width Largeur de la grille nécessaire au calcul.
     * @return Le décalage.
     */
    public abstract int offset(int width);

    /**
     * Index dans le tableau des arêtes de l'extrémité au sommet v
     * d'une arête dans la direction courante.
     *
     * @param v Un sommet
     * @return L'index de l'arête partant de v dans la direction courante.
     */
    public int edgeIndex(int v) {
      return values().length * v + ordinal();
    }

    /**
     * @return La direction opposée (Haut - Bas, Gauche - Droite).
     */
    public Direction opposite() {
      return values()[values().length - ordinal() - 1];
    }

    /**
     * <p>Détermine la direction à partir de la différence entre deux sommets.</p>
     *
     * <p>Vérifier si la différence est valide (càd si les sommets existent et sont
     * voisins dans la grille) doit avoir été fait au préalable.</p>
     *
     * @param width largeur de la grille
     * @param offset Différence entre deux sommets voisins
     * @return Une {@link Direction}
     */
    public static Direction fromOffset(int width, int offset) {
      // Magie noire, à remplacer par un switch-case...
      return values()[(int) Math.ceil((1 + 1e-6) * offset / width) + 1];
    }
  }

  /** Largeur */
  private final int width;

  /** hauteur */
  private final int height;

  /**
   * <p>Tableau des arêtes du graphe. Chaque sommet dispose de 4 slots successifs (un par direction)
   * situés à l'index {@code 4 * v}.</p>
   *
   * <p>L'ordre des slots est Haut-Gauche-Droite-Bas.</p>
   *
   * <p>Par rapport à des listes d'adjacences, permet un véritable accès indexé ainsi qu'une bien meilleure localité
   * spatiale.  Variante {@link java.util.BitSet} à étudier à l'occasion.</p>
   */
  private final boolean[] edges;

  /**
   * Construit une grille carrée.
   * @param side Côté de la grille.
   */
  public GridGraph(int side) {
    this(side, side);
  }

  /**
   * Construit une grille rectangulaire.
   * @param width Largeur de la grille.
   * @param height Hauteur de la grille.
   * @throws IllegalArgumentException si {@code width} ou {@code length} sont négatifs ou nuls.
   */
  public GridGraph(int width, int height) {
    if (width < 0 || height < 0)
      throw new IllegalArgumentException("Width: " + width + " and height: " + height + " must be non negative");

    this.width = width;
    this.height = height;
    this.edges = new boolean[Direction.values().length * nbVertices()];
  }

  @Override
  public List<Integer> neighbors(int v) {
    assertExists(v);

    List<Integer> neighbors = new ArrayList<>(Direction.values().length);
    for (Direction direction : Direction.values()) {
      if (hasEdge(v, direction))
        neighbors.add(v + direction.offset(width));
    }
    return neighbors;
  }

  @Override
  public List<Edge> edges() {
    List<Edge> edges = new ArrayList<>();

    for (int v = 0; v < nbVertices(); ++v) {
      // Haut et bas
      if (v >= width && hasEdge(v, Direction.UP))
        edges.add(new Edge(v - width, v));

      // Gauche et droite
      if (v % width > 0 && hasEdge(v, Direction.LEFT))
        edges.add(new Edge(v - 1, v));
    }

    return edges;
  }

  @Override
  public boolean areAdjacent(int u, int v) {
    assertExists(u);
    assertExists(v);

    // hasEdge basé sur la direction uniquqment, les sommets doivent être voisins dans la grille
    return areAdjacentInGrid(u, v) && hasEdge(u, Direction.fromOffset(width, v - u));
  }

  @Override
  public void addEdge(int u, int v) {
    assertExists(u);
    assertExists(v);

    if (!areAdjacentInGrid(u, v))
      throw new IllegalArgumentException("Can't create edge {" + u + "," + v + "}: " +
            "Only adjacent vertices in the grid can be bound");

    Direction dir = Direction.fromOffset(width, v - u);

    if (hasEdge(u, dir))
      throw new IllegalArgumentException("Edge {" + u + "," + v + "} already exists");

    setEdge(u, dir, true);
  }

  @Override
  public void removeEdge(int u, int v) {
    assertExists(u);
    assertExists(v);

    if (!areAdjacent(u, v))
      throw new IllegalArgumentException("Edge {" + u + "," + v + "} does not exist");

    setEdge(u, Direction.fromOffset(width, v - u), false);
  }

  @Override
  public int nbVertices() {
    return width * height;
  }

  @Override
  public boolean vertexExists(int v) {
    return v >= 0 && v < nbVertices();
  }

  @Override
  public int width() {
    return width;
  }

  @Override
  public int height() {
    return height;
  }

  // internal helpers

  private void assertExists(int v) {
    if (!vertexExists(v))
      throw new IndexOutOfBoundsException("Vertex " + v + " out of bounds. Domain: [0," + nbVertices() + "[");
  }

  /**
   * Vérifier si deux sommets sont adjacents dans la grille,
   * sans vérification sur les sommets.
   *
   * @param u Un sommet
   * @param v Un autre sommet
   * @return true si les sommets sont adjacents dans la grille, false sinon
   */
  private boolean areAdjacentInGrid(int u, int v) {
    int d = Math.abs(u - v);
    // Non égaux ET (au-dessus/au-dessous OU à gauche/à droite)
    return u != v && (d == width || d == 1 && u / width == v / width);
  }

  /**
   * Crée ou supprime une arête, sans vérification.
   * @param u Une extrémité
   * @param direction Direction dans laquelle créer l'arête
   * @param value true pour ajouter l'arête, false pour l'enlever
   */
  private void setEdge(int u, Direction direction, boolean value) {
    edges[direction.edgeIndex(u)] = value;
    edges[direction.opposite().edgeIndex(u + direction.offset(width))] = value;
  }

  /**
   * @param v Un sommet
   * @param direction Une direction
   * @return true si le sommet à un voisin dans la direction donnée, false sinon
   */
  private boolean hasEdge(int v, Direction direction) {
    return edges[direction.edgeIndex(v)];
  }

  // Public static helpers

  /**
   * Lie chaque sommet du graphe donné à tous ses voisins dans la grille.
   * @param graph Un graphe.
   * @throws NullPointerException si {@code graph} est null.
   */
  public static void bindAll(GridGraph graph) {
    for (int v = 0; v < graph.nbVertices(); ++v) {
      // Haut et bas
      if (v >= graph.width)
        graph.setEdge(v, Direction.UP, true);

      // Gauche et droite
      if (v % graph.width > 0)
        graph.setEdge(v, Direction.LEFT, true);
    }
  }
}
