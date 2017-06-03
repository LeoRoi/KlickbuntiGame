package matrixgui.model;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.*;

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
                GameMatrix matrix = block.getMatrix();
                String color = block.getColor();
                ArrayList<Data> deletionList = findSameColorBlocks(new ArrayList<>(), block.getData(), matrix, color);
                if (deletionList.size() == 0) {
                    AlertBox.display("Sorry", "There is no blocks matching the same color. Try again");
                } else {
                    // update matrix
                    updateMatrix(matrix);
                    // display matrix

                }
            }
        });
    }



    public static ArrayList<Data> findSameColorBlocks(ArrayList<Data> dataList, Data datagram, GameMatrix matrix, String color) {
        int row = datagram.getRow();
        int column = datagram.getColumn();

        Data right = checkRight(matrix, color, row, column);
        if (right != null) {
            dataList.add(right);
            // change matrix
            changeMatrix(matrix, right, datagram);
        }
        Data left = checkLeft(matrix, color, row, column);
        if (left != null) {
            dataList.add(left);
            changeMatrix(matrix, left, datagram);
        }
        Data top = checkTop(matrix, color, row, column);
        if (top != null) {
            dataList.add(top);
            changeMatrix(matrix, top, datagram);
        }

        Data down = checkDown(matrix, color, row, column);
        if (down != null) {
            dataList.add(down);
            changeMatrix(matrix, down, datagram);
        }

        System.out.println();
        if (dataList.size() > 0) {
        }


        System.out.println("Blocks to delete: " + dataList.size());
        matrix.show();

        for (Data data : dataList) {
            findSameColorBlocks(new ArrayList<>(), data, matrix, color);
        }

        return dataList;

    }

    private static Data checkDown(GameMatrix matrix, String color, int row, int column) {
        // check down
        String down;
        if (row != matrix.getM() - 1) {
            if (matrix.data[row + 1][column].equals(color)) {
                down = "same color " + color;
                return new Data(row + 1, column);
            } else {
                down = "different color";
            }
        } else {
            down = "no block";
        }
        System.out.println("Down " + down);
        return null;
    }

    private static Data checkTop(GameMatrix matrix, String color, int row, int column) {
        // check top
        String top;
        if (row != 0) {
            if (matrix.data[row - 1][column].equals(color)) {
                top = "same color " + color;
                return new Data(row - 1, column);
            } else {
                top = "different color";
            }
        } else {
            top = "no block";
        }
        System.out.println("Top " + top);
        return null;
    }

    private static Data checkLeft(GameMatrix matrix, String color, int row, int column) {
        // check left
        String left;
        if (column != 0) {
            if (matrix.data[row][column - 1].equals(color)) {
                left = "same color " + color;
                return new Data(row, column - 1);
            } else {
                left = "different color";
            }
        } else {
            left = "no block";
        }
        System.out.println("Left " + left);

        return null;
    }

    private static Data checkRight(GameMatrix matrix, String color, int row, int column) {
        // check right
        String right;
        if (column != matrix.getN() - 1) {
            if (matrix.data[row][column + 1].equals(color)) {
                right = "same color " + color;
                return new Data(row, column + 1);
            } else {
                right = "different color";
            }
        } else {
            right = "no block";
        }
        System.out.println("Right " + right);
        return null;
    }

    private static void changeMatrix(GameMatrix matrix, Data catched, Data source) {
        matrix.data[catched.getRow()][catched.getColumn()] = "BLACKY";
        matrix.data[source.getRow()][source.getColumn()] = "BLACKY";
    }

    private static void updateMatrix(GameMatrix matrix) {
        // 1) collect columns
        List<Column> lisOfColumns = matrix.collectColumnsToUpdate();
        for (Column column : lisOfColumns) {
            System.out.println("Column " + column.getColumnNumber() + "");
        }
        // 2) update columns vertically
        for (Column column : lisOfColumns) {
            updateOneColumn(column, matrix);




        }

        matrix.show();

        // 3) collect columns to update horizontally

        // 4) update columns horizontally
    }

    private static void updateOneColumn(Column column, GameMatrix matrix) {
        for (Data data : column.getListOfBoxes()) {
            
        }
    }

}