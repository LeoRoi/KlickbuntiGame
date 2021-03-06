package matrixgui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*
Created in IntelliJ IDEA 2017.1.4
so it may not run in other IDEs
 */

/**
 * Main class with {@link Application} for JavaFX
 * @autor Konstantin K. 559121
 * @autor Andrej L. 557966
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Method that gets resources and launch the main scene
     *
     * @param primaryStage Primary Stage to display a scene
     * @throws Exception when no resources available
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("layouts/matrix_display.fxml"));
        primaryStage.setTitle("Klickibunti");
        primaryStage.setScene(new Scene(root, 700, 400));
        primaryStage.show();
    }
}
