package gre.lab2.gui.impl;

import gre.lab2.graph.Graph;
import gre.lab2.graph.VertexLabelling;
import javafx.scene.paint.Color;

public final class StaticConfig {
  private StaticConfig(){}

  public static int startPoint(Graph graph) {
    return 0;
  }

  public static int wallThickness(int cellSide) {
    return Math.max(cellSide / 20, 1);
  }

  public static Color wallColor() {
    return Color.BLACK;
  }

  public static Color generatorColor(ObservableMaze maze, int v) {
    return switch(maze.getLabel(v)) {
      case PROCESSED -> Color.WHITE;
      case PENDING -> Color.BLACK;
      case PROCESSING -> Color.RED;
    };
  }

  public static Color solverCellColor(ObservableMaze maze, VertexLabelling<Integer> monitor, int v) {
    // Couleur différente par rapport au nombre de traitements ?
    return switch(monitor.getLabel(v)) {
      case -1 -> Color.LIGHTSEAGREEN;
      case 0 -> StaticConfig.generatorColor(maze, v);
      default -> Color.LIGHTBLUE;
    };
  }
}
