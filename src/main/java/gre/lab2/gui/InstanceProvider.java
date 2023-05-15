package gre.lab2.gui;

/**
 * Permet d'injecter différentes implémentations dans le controller.
 */
public interface InstanceProvider {
    /**
     * @return Une implémentation concrète de {@link MazeGenerator}.
     */
    MazeGenerator generator();

    /**
     * @return Une implémentation concrète de {@link MazeSolver}.
     */
    MazeSolver solver();
}
