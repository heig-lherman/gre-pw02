package gre.lab2.gui.impl;

import gre.lab2.graph.GridGraph;
import gre.lab2.gui.InstanceProvider;
import gre.lab2.gui.MazeGenerator;
import gre.lab2.gui.MazeSolver;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.function.*;

public final class MainViewController implements Initializable {
  @FXML private TitledPane generationGroup;
  @FXML private Slider gridSizeSlider;
  @FXML private CheckBox animateGen;
  @FXML private TitledPane solveGroup;
  @FXML private Pane playPauseGroup;
  @FXML private Slider delaySlider;
  @FXML private Button pauseBtn;
  @FXML private Pane canvasArea;
  @FXML private Canvas canvas;
  @FXML private Canvas overlay;

  private InstanceProvider instanceProvider;
  private MazePainter painter;
  private ObservableMaze maze;
  private Thread worker;
  private CompletableFuture<Void> pause = CompletableFuture.completedFuture(null);
  private boolean canceled;
  private Consumer<Integer> vertexSelector;
  private int source;
  private int destination;
  private MazeSolver solver;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    canvasArea.heightProperty().addListener(this::onResize);
    canvasArea.widthProperty().addListener(this::onResize);
    onSrcTool();
  }

  @FXML
  private void onGenerate() {
    if (instanceProvider == null) return;

    generationGroup.setDisable(true);
    solveGroup.setDisable(true);
    overlay.setVisible(false);
    playPauseGroup.setDisable(false);
    canceled = false;

    int side = (int) gridSizeSlider.getValue();
    GridGraph topology = new GridGraph(side);
    GridGraph.bindAll(topology);
    maze = new ObservableMaze(topology, new GridGraph(side));

    painter = new MazePainter(maze, canvas.getGraphicsContext2D())
          .setWallColor(StaticConfig.wallColor())
          .setCellColorF(v -> StaticConfig.generatorColor(maze, v));
    repaintMaze();
    repaintOverlay();

    MazeAnimation animation = newAnimation();
    if (animateGen.isSelected())
      maze.subscribe(animation);

    worker = new Thread(() -> {
      try {
        MazeGenerator builder = instanceProvider.generator();
        builder.generate(maze, StaticConfig.startPoint(maze));
      } catch (CanceledAnimationException ignored) {
      } finally {
        maze.unsubscribe(animation);
        source = 0;
        destination = maze.nbVertices() - 1;

        Platform.runLater(() -> {
          // Désactivation/activation des éléments de l'UI commence à devenir confuse, pattern état ?
          generationGroup.setDisable(false);
          playPauseGroup.setDisable(true);

          if (!animateGen.isSelected())
            repaintMaze();

          if (!canceled) {
            solveGroup.setDisable(false);
            overlay.setDisable(false);
            overlay.setVisible(true);
            repaintOverlay();
          }
        });
      }
    });
    worker.setDaemon(true);
    worker.start();
  }

  @FXML
  private void onSolve() {
    generationGroup.setDisable(true);
    solveGroup.setDisable(true);
    overlay.setDisable(true);
    playPauseGroup.setDisable(false);
    canceled = false;

    MazeAnimation animation = newAnimation();
    maze.subscribe(animation);

    SolverMonitor monitor = new SolverMonitor(maze.nbVertices(), animation);
    painter.setCellColorF(v -> StaticConfig.solverCellColor(maze, monitor, v));

    // Efface la solution précédente
    repaintMaze();

    worker = new Thread(() -> {
      try {
        List<Integer> path = solver.solve(maze, source, destination, monitor);
        for (int v : path) {
          monitor.setLabel(v, -1);
        }
      } catch (CanceledAnimationException ignored) {
      } finally {
        maze.unsubscribe(animation);
        Platform.runLater(() -> {
          generationGroup.setDisable(false);
          playPauseGroup.setDisable(true);
          solveGroup.setDisable(false);
          overlay.setDisable(false);
        });
      }
    });
    worker.setDaemon(true);
    worker.start();
  }

  @FXML
  private void onChooseBFS() {
    // Un seul algorithme disponible atm
    solver = instanceProvider.solver();
  }
  @FXML
  private void onSrcTool() {
    vertexSelector = v -> source = v;
  }
  @FXML
  private void onDstTool() {
    vertexSelector = v -> destination = v;
  }

  @FXML
  private void onSelectVertex(MouseEvent event) {
    // Très approximatif dans les bords droit et inférieur
    int l = painter.getCellSide() + 2 * painter.getWallThickness();
    int row = (int) event.getY() / l;
    int col = (int) event.getX() / l;

    // D'où cette vérification
    if (row < 0 || col < 0 || row >= maze.width() || col >= maze.height()) return;

    vertexSelector.accept(row * maze.width() + col);
    repaintOverlay();
  }

  @FXML
  private void onPlayPause() {
    setPause(pause.isDone());
  }

  @FXML
  private void onStop() {
    setPause(false);
    canceled = true;
    worker.interrupt();
  }

  private void setPause(boolean paused) {
    if (paused) {
      pause = new CompletableFuture<>();
      // Chars ⏵/⏸/⏹ ne fonctionnent pas avec les DE basés sur GTK, utilisation de texte
      pauseBtn.textProperty().setValue("Play");
    } else {
      pause.complete(null);
      pauseBtn.textProperty().setValue("Pause");
    }
  }

  private void onResize(Observable ignored) {
    if (maze == null) return;

    repaintMaze();
    repaintOverlay();
  }

  private void repaintOverlay() {
    overlay.setWidth(canvas.getWidth());
    overlay.setHeight(canvas.getHeight());

    GraphicsContext context = overlay.getGraphicsContext2D();
    context.clearRect(0, 0, overlay.getWidth(), overlay.getHeight());

    paintSelection(source, Color.GREEN);
    paintSelection(destination, Color.RED);
  }

  private void paintSelection(int vertex, Color color) {
    int x = painter.cellOffset(vertex % maze.width());
    int y = painter.cellOffset(vertex / maze.height());

    GraphicsContext context = overlay.getGraphicsContext2D();

    context.setFill(color);
    context.fillOval(x, y, painter.getCellSide(), painter.getCellSide());
  }

  private void repaintMaze() {
    int side = (int) Math.min(canvasArea.getWidth(), canvasArea.getHeight());

    // Permet de déterminer une taille de mur pertinente sans connaître à l'avance la taille réelle de la cellule
    int approximateCellSide = side / maze.width();
    int wallThickness = StaticConfig.wallThickness(approximateCellSide);
    int doubleThickness = 2 * wallThickness;

    // Garanti une taille min de 1 pour éviter une image noire
    int cellSide = Math.max(1, (side - doubleThickness) / maze.width() - doubleThickness);
    side = (cellSide + doubleThickness) * maze.width() + doubleThickness;

    canvas.setWidth(side);
    canvas.setHeight(side);

    painter.setWallThickness(wallThickness)
          .setCellSide(cellSide);
    painter.repaint();
  }

  public void setInstanceProvider(InstanceProvider instanceProvider) {
    this.instanceProvider = instanceProvider;
    solver = instanceProvider.solver();
  }

  private MazeAnimation newAnimation() {
    return new MazeAnimation(painter,
            () -> pause,
            // Fonction à décroissance géométrique fournissant un résultat dans [1;slider.max]
            // Permet de lisser l'accélération de l'animation lors de la sélection de faibles valeurs
            () -> (int) Math.pow(
                    delaySlider.getMax(),
                    (delaySlider.getMax() - delaySlider.getValue()) / delaySlider.getMax()));
  }
}