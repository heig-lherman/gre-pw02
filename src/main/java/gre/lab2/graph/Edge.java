package gre.lab2.graph;

/**
 * <p>Représente une arête <i>{u, v}</i>.</p>
 *
 * <p>S'agissant d'une paire, l'ordre n'est pas important et {@code new Edge(u, v).equals(new Edge(v, u))}
 * renvoie donc {@code true}.</p>
 *
 * @param u Une extrémité de l'arête.
 * @param v L'autre extrémité de l'arête.
 */
public record Edge(int u, int v) {
  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;

    if (obj instanceof Edge e)
      return u == e.u && v == e.v || u == e.v && v == e.u;

    return false;
  }

  @Override
  public int hashCode() {
    // Non uniforme, mais inutile de s'en soucier ici
    return u + v;
  }
}


