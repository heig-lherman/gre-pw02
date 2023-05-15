package gre.lab2.gui.impl;

import gre.lab2.graph.GridGraph2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.function.Function;

public final class MazePainter {

  private final GridGraph2D maze;
  private final GraphicsContext context;
  private Function<Integer, Color> cellColorF = v -> Color.WHITE;

  // Techniquement épaisseur d'un demi mur
  private int wallThickness = 1;
  private int cellSide = 100;
  private Color wallColor = Color.BLACK;

  public MazePainter(GridGraph2D maze, GraphicsContext context) {
    this.maze = maze;
    this.context = context;
  }

  public void drawWall(int u, int v) {
    if (! maze.areAdjacent(u, v)) {
      drawWall(u, v, wallColor, wallColor);
    } else {
      drawWall(u, v, cellColorF.apply(u), cellColorF.apply(v));
    }
  }

  private void drawWall(int u, int v, Color uColor, Color vColor) {
    int xu = col(u);
    int yu = row(u);
    int xv = col(v);
    int yv = row(v);

    // Swap des couleurs pour correspondre à l'ordre d'affichage (haut -> bas ou gauche -> droite)
    if (xv < xu || yv < yu) {
      Color tmp = uColor;
      uColor = vColor;
      vColor = tmp;
    }

    // Coordonnées du bord supérieur gauche du rectangle formée par les deux cellules
    int x = cellOffset(Math.min(xu, xv));
    int y = cellOffset(Math.min(yu, yv));

    if (xu == xv) {
      // Séparation horizontale
      drawRect(x, y + cellSide, cellSide, wallThickness, uColor);
      drawRect(x, y + cellSide + wallThickness, cellSide, wallThickness, vColor);
    } else {
      // Séparation verticale
      drawRect(x + cellSide, y, wallThickness, cellSide, uColor);
      drawRect(x + cellSide + wallThickness, y, wallThickness, cellSide, vColor);
    }
  }

  private void drawRect(int x, int y, int width, int height, Color color) {
    context.setFill(color);
    context.fillRect(x, y, width, height);
  }

  public void drawCell(int v) {
    drawRect(cellOffset(col(v)), cellOffset(row(v)), cellSide, cellSide, cellColorF.apply(v));

    for(int u : maze.neighbors(v)) {
      drawWall(u, v);
    }
  }

  public void repaint() {
    // Arrière-plan
    context.setFill(wallColor);
    context.fillRect(0 ,0, context.getCanvas().getWidth(), context.getCanvas().getHeight());

    // Cases et murs
    for (int u = 0; u < maze.nbVertices(); ++u) {
      drawCell(u);
      for (int v : maze.neighbors(u)) {
        if (u > v)
          drawWall(u, v);
      }
    }
  }

  // Getters

  public int getCellSide() {
    return cellSide;
  }

  public int getWallThickness() {
    return wallThickness;
  }

  // Fluent setters

  public MazePainter setCellColorF(Function<Integer, Color> cellColorF) {
    this.cellColorF = cellColorF;
    return this;
  }

  public MazePainter setWallColor(Color wallColor) {
    this.wallColor = wallColor;
    return this;
  }

  public MazePainter setWallThickness(int wallThickness) {
    this.wallThickness = wallThickness;
    return this;
  }

  public MazePainter setCellSide(int cellSide) {
    this.cellSide = cellSide;
    return this;
  }

  // Helpers

  public int cellOffset(int pos) {
    return 2 * wallThickness + pos * (cellSide + 2 * wallThickness);
  }

  private int row(int v) {
    return v / maze.width();
  }

  private int col(int v) {
    return v % maze.width();
  }
}


