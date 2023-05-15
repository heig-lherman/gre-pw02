package gre.lab2.groupe11;

import gre.lab2.gui.InstanceProvider;
import gre.lab2.gui.MazeGenerator;
import gre.lab2.gui.MazeSolver;
import gre.lab2.gui.impl.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public final class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(InstanceProvider.class.getResource("mainView.fxml"));
        Parent parent = fxmlLoader.load();
        Scene scene = new Scene(parent, 800, 600);
        stage.setTitle("Shining generator");
        stage.setScene(scene);

        MainViewController controller = fxmlLoader.getController();
        controller.setInstanceProvider(new InstanceProvider() {
            @Override
            public MazeGenerator generator() {
                return new KruskalMazeGenerator();
            }

            @Override
            public MazeSolver solver() {
                return new BfsSolver();
            }
        });

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}