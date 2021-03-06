package matrixgui.model;

import matrixgui.GameUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class that is programming representation of the regular mathematical matrix
 * with most common used mathematical methods
 */
final public class GameMatrix implements Serializable {
    private final int M;
    private final int N;
    public final String[][] data;

    public int getM() {
        return M;
    }

    public int getN() {
        return N;
    }

    public String[][] getData() {
        return data;
    }

    /**
     * Constructor to create empty matrix with specified dimension
     *
     * @param M Number of rows
     * @param N Number of columns
     */
    public GameMatrix(int M, int N) {
        this.M = M;
        this.N = N;
        data = new String[M][N];
    }

    /**
     * Constructor to create matrix based on 2-dim array
     *
     * @param data Dimensions with stored color
     */
    public GameMatrix(String[][] data) {
        M = data.length;
        N = data[0].length;
        this.data = new String[M][N];
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                this.data[i][j] = data[i][j];
    }

    /**
     * Creates Matrix with random numbers based on the specified dimension
     *
     * @param rows    Number of rows
     * @param columns Number of columns
     * @return Matrix with random numbers with specified dimension
     */
    public static GameMatrix randomWithReservedState(int rows, int columns, int numberOfColors) {
        GameUtils.init(5);
        GameMatrix A = new GameMatrix(rows, columns);
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++) {
                String result = GameUtils.getRandomColor(numberOfColors);
                A.data[i][j] = result;
            }
        return A;
    }

    /**
     * Creates Matrix with random numbers from 2 to 6 dimensions
     *
     * @return Matrix with random numbers from 2 to 6 dimensions
     */
    public static GameMatrix random(int numberOfColors) {
        GameUtils.init(5);
        int M = (int) createRandomDouble(2, 7);
        int N = (int) createRandomDouble(2, 7);
        GameMatrix A = new GameMatrix(M, N);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++) {
                String result = GameUtils.getRandomColor(numberOfColors);
                A.data[i][j] = result;
            }
        return A;
    }

    /**
     * Creates random double number based on the specified range
     *
     * @param start Start of the range
     * @param end   End of the range
     * @return Random double number based on the specified range
     */
    public static double createRandomDouble(int start, int end) {
        double randomDouble = new Random().nextDouble();
        return start + (randomDouble * (end - start));
    }


    /**
     * Method to show matrix in the standard output (testing purposes)
     */
    public void show() {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++)
                System.out.printf(data[i][j] + "  ");
            System.out.println();
        }
    }

    public List<Column> collectColumnsToUpdateVertically() {
        List<Column> list = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            boolean ok = false;
            List<Data> listOfBoxes = new ArrayList<>();
            for (int j = 0; j < M; j++) {
                if (this.data[j][i].equals("BLACKY")) {
                    ok = true;
                    Data data = new Data(j, i);
                    listOfBoxes.add(data);
                }
            }
            if (ok) {
                Column column = new Column(i, listOfBoxes);
                list.add(column);
            }
        }
        return list;
    }

    public List<Column> collectColumnsToUpdateHorizontally(int count) {
        List<Column> list = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            int incrementer = 0;
            for (int j = 0; j < M; j++) {
                if (this.data[j][i].equals("SWITCHY")) {
                    incrementer++;
                }
            }
            if (incrementer == this.M && i > count) {
                Column column = new Column(i, null);
                list.add(column);
            }
        }
        return list;
    }

    /**
     * collect all the deleted pieces
     * @return list with black blocks
     */
    public List<Data> findBlackies() {
        List<Data> list = new ArrayList<>();
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                if (this.data[i][j].equals("BLACKY")) {
                    list.add(new Data(i, j));
                }
            }
        }
        return list;
    }

    /**
     * collect all pieces with similar color, so they may change their color to black
     * @return list with switch blocks
     */
    public List<Column> findSwitchies() {
        List<Column> list = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            int incrementer = 0;
            for (int j = 0; j < M; j++) {
                if (this.data[j][i].equals("SWITCHY")) {
                    incrementer++;
                }
            }
            if (incrementer == this.M) {
                Column column = new Column(i, null);
                list.add(column);
            }
        }
        return list;
    }
}