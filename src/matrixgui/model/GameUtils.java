package matrixgui.model;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class GameUtils {

    public static GridPane createGridPaneForGame(Matrix matrix) {
        GridPane gridPane = new GridPane();
        gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(8);
        gridPane.setHgap(10);

        for (int i = 0; i < matrix.getM(); i++) {
            for (int j = 0; j < matrix.getN(); j++) {
                Button button = new Button();
                String enteredByUser = "abcdef";
                button.setStyle("-fx-background-color: #" + enteredByUser);
                button.setMaxHeight(100);
                button.setWrapText(true);
                button.setText("Test test");
                gridPane.add(button, j, i);
            }
        }

        return gridPane;
    }
}
