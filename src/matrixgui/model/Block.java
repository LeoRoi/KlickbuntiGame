package matrixgui.model;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class Block extends Label {

    GameMatrix matrix;
    String color;
    int row;
    int column;
    Data data;

    public Block(String text, GameMatrix matrix, String color, int row, int column) {
        super("     ");
        this.matrix = matrix;
        this.color = color;
        this.row = row;
        this.column = column;
        this.data = new Data(row, column);
    }

    public String getColor() {
        return color;
    }

    public void setColor(String storedColor) {
        this.color = storedColor;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public GameMatrix getMatrix() {
        return matrix;
    }

    public Data getData() {
        return data;
    }
}
