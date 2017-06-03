package matrixgui;

import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import matrixgui.model.AlertBox;
import matrixgui.model.GameMatrix;
import matrixgui.model.GameUtils;
import matrixgui.model.Matrix;
import matrixgui.utils.MatrixUtils;

import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Class that is responsible for displaying GUI and handling user events
 */
public class MatrixDisplayController implements Initializable {

    // data
    private GameMatrix matrixA;

    // UI
    // matrices calculations
    public VBox displayMatrixVbox;
    public HBox matrixAndMatrixHBox;
    private GridPane gridPaneA;
    private Label label;
    private Button undoButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Matrix
        matrixA = GameMatrix.randomWithReservedState(15, 15);
        GameUtils.undoMatrix = new GameMatrix(matrixA.data);
        GameUtils.undoPoints = 0;
        GameUtils.undoCountSwitchies = 0;
//        MatrixUtils.saveMatrix(matrixA, "matrixA");
        gridPaneA = GameUtils.createGridPaneFromGameMatrix(matrixA);
        matrixAndMatrixHBox.getChildren().add(gridPaneA);

        // RATING
        label = new Label();
        displayMatrixVbox.getChildren().add(label);
        label.setText("RATING");

        // UNDO
        undoButton = new Button();
        displayMatrixVbox.getChildren().add(undoButton);
        undoButton.setText("UNDO");
        undoButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // undo matrix
                if (GameUtils.undoMatrix != null) {
                    matrixAndMatrixHBox.getChildren().removeAll(gridPaneA);
                    gridPaneA = GameUtils.createGridPaneFromGameMatrix(GameUtils.undoMatrix);
                    matrixAndMatrixHBox.getChildren().add(gridPaneA);
                    GameUtils.undoMatrix = new GameMatrix(GameUtils.undoMatrix.data);
                }
                // undo points
                    displayMatrixVbox.getChildren().removeAll(label);
                    label = new Label();
                    label.setMaxWidth(150);
                    label.setText("RATING : " + String.valueOf(GameUtils.undoPoints));
                    GameUtils.points = GameUtils.undoPoints;
                    displayMatrixVbox.getChildren().add(1, label);
            }
        });

        GameUtils gameUtils = new GameUtils(new GameUtils.Updatable() {
            @Override
            public void refresh(GameMatrix matrix) {
                matrixAndMatrixHBox.getChildren().removeAll(gridPaneA);
                gridPaneA = GameUtils.createGridPaneFromGameMatrix(matrix);
                matrixAndMatrixHBox.getChildren().add(gridPaneA);
            }

            @Override
            public void rate(int points) {
                displayMatrixVbox.getChildren().removeAll(label);
                label = new Label();
                label.setMaxWidth(150);
                label.setText("RATING : " + String.valueOf(points));
                displayMatrixVbox.getChildren().add(1, label);
            }


        });


    }

}
