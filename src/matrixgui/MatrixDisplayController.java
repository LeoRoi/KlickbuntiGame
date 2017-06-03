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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Matrix A
        matrixA = GameMatrix.randomWithReservedState(5, 5);
//        MatrixUtils.saveMatrix(matrixA, "matrixA");
        gridPaneA = GameUtils.createGridPaneFromGameMatrix(matrixA);
//        MatrixUtils.displayMatrixOnGridPane(gridPaneA, matrixA);
        matrixAndMatrixHBox.getChildren().add(gridPaneA);

        GameUtils gameUtils = new GameUtils(new GameUtils.Updatable() {
            @Override
            public void update(GameMatrix matrix) {
                matrixAndMatrixHBox.getChildren().removeAll(gridPaneA);
                gridPaneA = GameUtils.createGridPaneFromGameMatrix(matrix);
                matrixAndMatrixHBox.getChildren().add(gridPaneA);
            }
        });
    }

}
