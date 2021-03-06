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

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Class that is responsible for displaying GUI and handling user events
 */
public class MatrixDisplayController implements Initializable {

    // variable declarations

    // data
    private GameMatrix matrixA;

    // UI
    // game field
    public HBox matrixAndMatrixHBox;
    private GridPane gridPaneA;
    private Label label;
    private Button undoButton;
    private VBox secondColumnVbox;

    // Utils
    GameUtils gameUtils;

    int numberOfColors;

    // Control panel: Color block
    public Label labelPromptForColor;
    public TextField textFieldPromptForColor;
    public Button buttonPromptForColor;

    // Control panel: game field block
    public Label labelPromptForLines;
    public TextField textFieldPromptForRows;
    public TextField textFieldPromptForColumns;
    public Button buttonPromptForRowsAndColumns;

    /**
     * Start the game at once after execution with these params
     * and create a control panel at the right side
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        numberOfColors = 5;

        //Matrix
        matrixA = GameMatrix.randomWithReservedState(20, 20, numberOfColors);
        GameUtils.undoMatrix = new GameMatrix(matrixA.data);
        GameUtils.undoPoints = 0;
        GameUtils.undoCountSwitchies = 0;
        gridPaneA = GameUtils.createGridPaneFromGameMatrix(matrixA);
        matrixAndMatrixHBox.getChildren().add(0, gridPaneA);

        // SecondColumnVbox
        secondColumnVbox = new VBox();
        secondColumnVbox.setPadding(new Insets(15, 0, 0, 0));
        secondColumnVbox.setSpacing(20D);
        matrixAndMatrixHBox.getChildren().add(1, secondColumnVbox);

        // RATING
        label = new Label();
        secondColumnVbox.getChildren().add(0, label);
        label.setText("RATING");

        // UNDO button and its action
        undoButton = new Button();
        secondColumnVbox.getChildren().add(1, undoButton);
        undoButton.setText("UNDO");
        undoButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

            /**
             * action for UNDO button
             * @param event
             */
            @Override
            public void handle(MouseEvent event) {

                // undo matrix
                if (GameUtils.undoMatrix != null) {
                    matrixAndMatrixHBox.getChildren().removeAll(gridPaneA);
                    gridPaneA = GameUtils.createGridPaneFromGameMatrix(GameUtils.undoMatrix);
                    matrixAndMatrixHBox.getChildren().add(0, gridPaneA);
                    GameUtils.undoMatrix = new GameMatrix(GameUtils.undoMatrix.data);
                }

                // undo points
                secondColumnVbox.getChildren().removeAll(label);
                label = new Label();
                label.setMaxWidth(150);
                label.setText("RATING : " + String.valueOf(GameUtils.undoPoints));
                GameUtils.points = GameUtils.undoPoints;
                secondColumnVbox.getChildren().add(0, label);
            }
        });


        gameUtils = new GameUtils(new GameUtils.Updatable() {
            /**
             * reload game field
             * @param matrix info about current game state
             */
            @Override
            public void refresh(GameMatrix matrix) {
                matrixAndMatrixHBox.getChildren().removeAll(gridPaneA);
                gridPaneA = GameUtils.createGridPaneFromGameMatrix(matrix);
                matrixAndMatrixHBox.getChildren().add(0, gridPaneA);
            }

            /**
             * reload control panel
             * @param points rating
             */
            @Override
            public void rate(int points) {
                secondColumnVbox.getChildren().removeAll(label);
                label = new Label();
                label.setMaxWidth(150);
                label.setText("RATING : " + String.valueOf(points));
                secondColumnVbox.getChildren().add(0, label);
            }
        });

        // miscellanea reload
        setPromptForColor();
        setListenersPromptForColor();
        setPromptForRows();
        setListenersPromptForRowsAndColumns();
    }

    /**
     * control panel: place visual elements for color selection
     */
    public void setPromptForColor() {
        labelPromptForColor = new Label();
        labelPromptForColor.setText("Please enter the number of colors");
        secondColumnVbox.getChildren().add(labelPromptForColor);

        textFieldPromptForColor = new TextField();
        textFieldPromptForColor.setMaxWidth(250D);
        textFieldPromptForColor.setPromptText("Number of colors: between 2 and 5");
        secondColumnVbox.getChildren().add(textFieldPromptForColor);

        buttonPromptForColor = new Button();
        buttonPromptForColor.setMaxWidth(100D);
        buttonPromptForColor.setMaxHeight(100D);
        buttonPromptForColor.setText("Set colors");
        secondColumnVbox.getChildren().add(buttonPromptForColor);
    }

    /**
     * control panel: action for color selection
     */
    private void setListenersPromptForColor() {
        buttonPromptForColor.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                numberOfColors = 5;
                try {
                    numberOfColors = Integer.parseInt(textFieldPromptForColor.getText().trim());
                }
                catch (NumberFormatException e) {
                    e.printStackTrace();
                    AlertBox.display("Error", "Please use numbers between 2 and 5");
                }

                if (numberOfColors > 1 && numberOfColors < 6) {
                    // UNDO and RATING reset
                    GameUtils.points = 0;
                    GameUtils.undoPoints = 0;
                    GameMatrix matrix = GameMatrix.randomWithReservedState(gameUtils.undoMatrix.getM(),gameUtils.undoMatrix.getN(), numberOfColors);
                    gameUtils.handler.refresh(matrix);
                    gameUtils.handler.rate(GameUtils.points);
                    gameUtils.undoMatrix = matrix;
                }
                else {
                    AlertBox.display("Error", "Please use numbers between 2 and 5");
                }
            }
        });
    }

    /**
     * control panel: place visual elements for rows & columns selection
     */
    private void setPromptForRows() {
        labelPromptForLines = new Label();
        labelPromptForLines.setText("Please enter the number of rows and columns");
        secondColumnVbox.getChildren().add(labelPromptForLines);

        textFieldPromptForRows = new TextField();
        textFieldPromptForRows.setMaxWidth(250D);
        textFieldPromptForRows.setPromptText("Number of rows: between 10 and 40");
        secondColumnVbox.getChildren().add(textFieldPromptForRows);

        textFieldPromptForColumns = new TextField();
        textFieldPromptForColumns.setMaxWidth(250D);
        textFieldPromptForColumns.setPromptText("Number of columns: between 10 and 40");
        secondColumnVbox.getChildren().add(textFieldPromptForColumns);

        buttonPromptForRowsAndColumns = new Button();
        buttonPromptForRowsAndColumns.setMaxWidth(100D);
        buttonPromptForRowsAndColumns.setMaxHeight(100D);
        buttonPromptForRowsAndColumns.setText("Set game field");
        secondColumnVbox.getChildren().add(buttonPromptForRowsAndColumns);
    }

    /**
     * control panel: action for rows & columns selection
     */
    private void setListenersPromptForRowsAndColumns() {
        buttonPromptForRowsAndColumns.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Integer rows = null;
                Integer columns = null;

                // handle incorrect input
                try {
                    rows = Integer.parseInt(textFieldPromptForRows.getText().trim());
                    columns = Integer.parseInt(textFieldPromptForColumns.getText().trim());
                }
                catch (NumberFormatException e) {
                    e.printStackTrace();
                    AlertBox.display("Error", "Please use numbers between 10 and 40");
                }
                if ((rows > 9 && rows < 41) && (columns > 9 && columns < 41)) {
                    // UNDO and RATING reset
                    GameUtils.points = 0;
                    GameUtils.undoPoints = 0;
                    GameMatrix matrix = GameMatrix.randomWithReservedState(rows, columns, numberOfColors);
                    gameUtils.handler.refresh(matrix);
                    gameUtils.handler.rate(GameUtils.points);
                    gameUtils.undoMatrix = matrix;
                }
                else {
                    AlertBox.display("Error", "Please use numbers between 10 and 40");
                }
            }
        });
    }
}
