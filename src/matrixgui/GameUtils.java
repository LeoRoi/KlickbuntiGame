package matrixgui;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import matrixgui.model.*;

import java.util.*;

/**
 * intelligent back-bone of the game
 */
public class GameUtils {

    public static Updatable handler;

    public interface Updatable {
        void refresh(GameMatrix matrix);
        void rate(int points);
    }

    public GameUtils(Updatable handler) {
        this.handler = handler;
    }

    // default constants
    public static final String TEST_STRING = "          ";

    // colors
    public static final String COLOR_ONE = "f4ad42";    //yellow
    public static final String COLOR_TWO = "2e9645";    //green
    public static final String COLOR_THREE = "3b257c";  //blue
    public static final String COLOR_FOUR = "c13f2e";   //orange
    public static final String COLOR_FIVE = "820987";   //purple
    public static List<String> colors;

    public static int points;
    public static int countSwitchies;
    public static int undoCountSwitchies;
    public static GameMatrix undoMatrix;
    public static int undoPoints;

    /**
     * Initialize a list with different colors
     * @param numberOfColors user selection
     */
    public static void init(int numberOfColors) {
        colors = new ArrayList<>();
        switch (numberOfColors) {
            case 1:
                colors.add(COLOR_ONE);
                break;
            case 2:
                colors.add(COLOR_ONE);
                colors.add(COLOR_TWO);
                break;
            case 3:
                colors.add(COLOR_ONE);
                colors.add(COLOR_TWO);
                colors.add(COLOR_THREE);
                break;
            case 4:
                colors.add(COLOR_ONE);
                colors.add(COLOR_TWO);
                colors.add(COLOR_THREE);
                colors.add(COLOR_FOUR);
                break;
            case 5:
                colors.add(COLOR_ONE);
                colors.add(COLOR_TWO);
                colors.add(COLOR_THREE);
                colors.add(COLOR_FOUR);
                colors.add(COLOR_FIVE);
                break;
            default:
                colors.add(COLOR_ONE);
                colors.add(COLOR_TWO);
                colors.add(COLOR_THREE);
                colors.add(COLOR_FOUR);
                colors.add(COLOR_FIVE);
        }
    }

    /**
     * convert matrix to grid pane
     * @param matrix to be converted
     * @return created grid pane
     */
    public static GridPane createGridPaneFromGameMatrix(GameMatrix matrix) {
        init(5);
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        for (int i = 0; i < matrix.getM(); i++) { // rows
            for (int j = 0; j < matrix.getN(); j++) { // columns
                Block block = new Block(TEST_STRING, matrix, matrix.data[i][j], i, j);
                if (block.getColor().equals("SWITCHY")) {
                    block.setStyle("-fx-background-color: #" + "000000; \n" + "-fx-border-color: white;");
                }
                else {
                    block.setStyle("-fx-background-color: #" + block.getColor() + "; \n" + "-fx-border-color: white;");
                }

                block.setMinHeight(15);
                block.setMinWidth(15);
                setListener(block);

                // object, column & row index of the cell in which the object should be displayed
                gridPane.add(block, j, i);
            }
        }
        return gridPane;
    }

    /**
     * get a random color from the list
     * @param numberOfColors chosen by user
     * @return chosen random color
     */
    public static String getRandomColor(int numberOfColors) {
        init(numberOfColors);
        Collections.shuffle(colors);
        String color = colors.get(0);
        colors.clear();
        return color;
    }

    /**
     * Actions beyond each stone-click:
     * save current state, calculate, update
     * @param block game piece
     */
    public static void setListener(Block block) {
        block.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                GameMatrix matrix = block.getMatrix();
                undoMatrix = new GameMatrix(matrix.data);
                undoPoints = points;
                countSwitchies = undoCountSwitchies;
                undoCountSwitchies = countSwitchies;

                String color = block.getColor();
                ArrayList<Data> deletionList = findSameColorBlocks(new ArrayList<>(), block.getData(), matrix, color);
                if (deletionList.size() == 0) {
                    List<Column> checkies = matrix.findSwitchies();

                    if (checkies.size() > matrix.getN() / 10 * 9) {
                        AlertBox.display("You lost", "Try again");
                    }
                    else {
                        AlertBox.display("Sorry", "There are no blocks matching the same color. Try again");
                    }
                }
                else {
                    // find blackies
                    List<Data> blackies = matrix.findBlackies();
                    int n = blackies.size();
                    points += 2 * n - 2;

                    // reload matrix
                    updateMatrix(matrix);

                    // rate
                    List<Column> switchies = matrix.findSwitchies();
                    if (switchies.size() == matrix.getN()) {
                        AlertBox.display("Hurray", "You won");
                    }

                    // reload points
                    points += switchies.size() * 10 - (countSwitchies * 10);
                    System.out.println("Points: " + points);
                    countSwitchies = switchies.size();
                    handler.rate(points);
                }
            }
        });
    }

    /**
     * the checking algorithm: whether the neighborhood has similar color or not
     * @param dataList gathers info about what to delete
     * @param datagram provides actual coordinates
     * @param matrix
     * @param color
     * @return
     */
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
        System.out.println("Blocks to delete: " + dataList.size());
        matrix.show();

        for (Data data : dataList) {
            findSameColorBlocks(new ArrayList<>(), data, matrix, color);
        }
        return dataList;
    }

    /**
     * see if the down-neighbour has the same color
     * @param matrix
     * @param color
     * @param row
     * @param column
     * @return
     */
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

    /**
     * see if the top-neighbour has the same color
     * @param matrix
     * @param color
     * @param row
     * @param column
     * @return
     */
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

    /**
     * see if the left-neighbour has the same color
     * @param matrix
     * @param color
     * @param row
     * @param column
     * @return
     */
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

    /**
     * see if the right-neighbour has the same color
     * @param matrix
     * @param color
     * @param row
     * @param column
     * @return
     */
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

    /**
     * change the stone color to black
     * @param matrix
     * @param catched
     * @param source
     */
    private static void changeMatrix(GameMatrix matrix, Data catched, Data source) {
        matrix.data[catched.getRow()][catched.getColumn()] = "BLACKY";
        matrix.data[source.getRow()][source.getColumn()] = "BLACKY";
    }

    /**
     * reload game field
     * @param matrix
     */
    private static void updateMatrix(GameMatrix matrix) {
        // 1) collect columns
        List<Column> lisOfColumns = matrix.collectColumnsToUpdateVertically();
        for (Column column : lisOfColumns) {
            System.out.println("Column " + column.getColumnNumber() + "");
        }
        // 2) refresh columns vertically
        for (Column column : lisOfColumns) {
            updateOneColumnVertically(column, matrix);
        }
        // 3) collect columns to refresh horizontally
        // 4) refresh columns horizontally
        for (int i = 0; i < matrix.getN(); i++) {
            updateMatrixPart2(matrix, 0);
        }

        matrix.show();
        handler.refresh(matrix);
    }

    /**
     * reload game field
     * @param matrix
     * @param count
     */
    private static void updateMatrixPart2(GameMatrix matrix, int count) {
        List<Column> lisOfColumns2 = matrix.collectColumnsToUpdateHorizontally(count);
        System.out.println(lisOfColumns2.size());
        if (lisOfColumns2.size() > count) {
            for (Column column : lisOfColumns2) {
                updateOneColumnHorizontally(column, matrix);
            }
        }
    }

    /**
     * reload just a single column horizontally
     * @param column
     * @param matrix
     */
    private static void updateOneColumnHorizontally(Column column, GameMatrix matrix) {
        // refresh column one
        int r = matrix.getM() - 1;
        int c = column.getColumnNumber();
        if (c != 0) {
            matrix.data[r][c] = matrix.data[r][c - 1];
            matrix.data[r][c - 1] = "SWITCHY";
        }
        for (int i = 1; i < r + 1; i++) {
            matrix.data[r - i][c] = matrix.data[r - i][c - 1];
            matrix.data[r - i][c - 1] = "SWITCHY";
        }
    }

    /**
     * reload just a single column vertically
     * @param column
     * @param matrix
     */
    private static void updateOneColumnVertically(Column column, GameMatrix matrix) {
        for (Data data : column.getListOfBoxes()) {
            // refresh column once
            int r = data.getRow();
            int c = data.getColumn();
            if (r != 0) {
                matrix.data[r][c] = matrix.data[r - 1][c];
            }
            for (int i = 1; i < r; i++) {
                matrix.data[r - i][c] = matrix.data[r - i - 1][c];
            }
            matrix.data[0][column.getColumnNumber()] = "SWITCHY";
        }
    }
}