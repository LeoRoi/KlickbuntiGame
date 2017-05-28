package matrixgui.model;

import javafx.scene.control.Button;

public class Block extends Button {

    GameMatrix matrix;
    String color;
    int row;
    int column;

    public Block(String text, GameMatrix matrix, String color, int row, int column) {
        super(text);
        this.matrix = matrix;
        this.color = color;
        this.row = row;
        this.column = column;
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
}
