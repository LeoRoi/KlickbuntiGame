package matrixgui.model;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameUtils {

    // default constants
    public static final String STATIC_COLOR = "000000";
    public static final String TEST_STRING = "          ";

    // colors
    public static final String COLOR_ONE = "f4ad42";
    public static final String COLOR_TWO = "2e9645";
    public static final String COLOR_THREE = "3b257c";
    public static final String COLOR_FOUR = "c13f2e";
    public static final String COLOR_FIVE = "820987";

    public static List<String> colors = new ArrayList<>();
    private static Block listener;

    public static void init() {
        colors.add(COLOR_ONE);
        colors.add(COLOR_TWO);
        colors.add(COLOR_THREE);
        colors.add(COLOR_FOUR);
        colors.add(COLOR_FIVE);
    }

    public static GridPane createGridPaneFromGameMatrix(GameMatrix matrix) {
        init();
        GridPane gridPane = new GridPane();
        gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(8);
        gridPane.setHgap(10);

        for (int i = 0; i < matrix.getM(); i++) {
            for (int j = 0; j < matrix.getN(); j++) {
                Block block = new Block(TEST_STRING, matrix, matrix.data[i][j], i, j);
                block.setStyle("-fx-background-color: #" + block.getColor());
                block.setMaxHeight(100);
                block.setWrapText(true);
                block.setText(TEST_STRING);
                setListener(block);
                gridPane.add(block, j, i);
            }
        }
        return gridPane;
    }

    public static String getRandomColor() {
        init();
        Collections.shuffle(colors);
        String color = colors.get(0);
        colors.clear();
        return color;
    }

    public static void setListener(Block block) {
        block.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                List<Data> deletionList = findSameColorBlocks(block);
                if (deletionList.size() == 0) {
                    AlertBox.display("Sorry", "There is no blocks matching the same color. Try again");
                } else {
                    createNewMatrix(deletionList, block.getMatrix());
                }
            }
        });
    }

    private static GameMatrix createNewMatrix(List<Data> deletionList, GameMatrix oldMatrix) {
        oldMatrix.show();

        for (Data data : deletionList) {
            deleteBlock(oldMatrix, data);
        }


        return null;
    }

    private static void deleteBlock(GameMatrix oldMatrix, Data data) {
        oldMatrix.data[data.getRow()][data.getColumn()] = oldMatrix.data[data.getRow()][data.getColumn() - 1];
    }

    private static List<Data> findSameColorBlocks(Block block) {
        List<Data> dataList = new ArrayList<>();
        String color = block.getColor();
        int row = block.getRow();
        int column = block.getColumn();
        GameMatrix matrix = block.getMatrix();

        // check right
        String right;
        if (column != matrix.getN() - 1) {
            if (matrix.data[row][column + 1].equals(color)) {
                right = "same color " + color;
                Data data = new Data(row, column);
                dataList.add(data);
            } else {
                right = "different color";
            }
        } else {
            right = "no block";
        }
        System.out.println("Right " + right);

        // check left
        String left;
        if (column != 0) {
            if (matrix.data[row][column - 1].equals(color)) {
                left = "same color " + color;
                Data data = new Data(row, column);
                dataList.add(data);
            } else {
                left = "different color";
            }
        } else {
            left = "no block";
        }
        System.out.println("Left " + left);

        // check top
        String top;
        if (row != 0) {
            if (matrix.data[row - 1][column].equals(color)) {
                top = "same color " + color;
                Data data = new Data(row, column);
                dataList.add(data);
            } else {
                top = "different color";
            }
        } else {
            top = "no block";
        }
        System.out.println("Top " + top);

        // check down
        String down;
        if (row != matrix.getM() - 1) {
            if (matrix.data[row + 1][column].equals(color)) {
                down = "same color " + color;
                Data data = new Data(row, column);
                dataList.add(data);
            } else {
                down = "different color";
            }
        } else {
            down = "no block";
        }
        System.out.println("Down " + down);
        System.out.println();

        return dataList;
    }
}